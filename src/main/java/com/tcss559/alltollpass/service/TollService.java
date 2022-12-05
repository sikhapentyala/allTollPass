package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.TollRequest;
import com.tcss559.alltollpass.entity.toll.Toll;
import com.tcss559.alltollpass.entity.toll.TollRate;
import com.tcss559.alltollpass.entity.traveler.VehicleType;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.TollNotFoundException;
import com.tcss559.alltollpass.model.request.toll.LocationRequest;
import com.tcss559.alltollpass.model.request.toll.TollRateRequest;
import com.tcss559.alltollpass.model.response.toll.LocationResponse;
import com.tcss559.alltollpass.repository.toll.TollRateRepository;
import com.tcss559.alltollpass.repository.toll.TollRepository;
import com.tcss559.alltollpass.repository.toll.TollTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TollService {

    @Autowired
    TollRepository tollRepository;

    @Autowired
    TollRateRepository tollRateRepository;

    @Autowired
    TollTransactionRepository tollTransactionRepository;

    @Autowired
    UserService userService;

    @Autowired
    TravelerService travelerService;

    public LocationResponse updateLocation(LocationRequest locationRequest) {
        Toll toll = tollRepository.save(Toll.builder()
                .location(locationRequest.getLocation())
                .userId(locationRequest.getUserId())
                .build()
        );

        return LocationResponse.builder()
                .userId(toll.getUserId())
                .location(toll.getLocation())
                .build();
    }

    public void upsertTollRates(TollRateRequest tollRateRequest) {
        Toll toll = tollRepository.findByIdAndUserId(tollRateRequest.getTollId(), tollRateRequest.getUserId()).orElseThrow(() -> new TollNotFoundException("Toll not found"));
        Optional<TollRate> optionalRate = tollRateRepository.findByTollId(toll.getId());
        TollRate rate;
        if(optionalRate.isPresent()){
            rate = optionalRate.get();
            rate.setTollRate(tollRateRequest.getTollRate());
            rate.setUpdatedTimestamp(LocalDateTime.now());

        }else{
            rate = TollRate.builder()
                    .tollId(toll.getId())
                    .tollRate(tollRateRequest.getTollRate())
                    .vehicleType(tollRateRequest.getVehicleType())
                    .build();
        }
        try{
            tollRateRepository.save(rate);
            //TODO: return the tollrate response
        }catch (RuntimeException e){
            throw new DatabaseException(e);
        }


    }

    public void makeTransaction(TollRequest request) {
        Toll toll = tollRepository.findById(request.getTollId()).orElseThrow(() -> new TollNotFoundException("No Toll found"));
        VehicleType vehicleType = travelerService.getVehicleTypeFromRfid(request.getRfid());
        TollRate rate = tollRateRepository.findByTollIdAndVehicleType(toll.getId(), vehicleType).orElseThrow(() -> new RuntimeException("Toll Rate not found for the vehicle type: " + vehicleType));
        rate.getTollRate();
        //TODO: logic to handle toll transaction


    }
}
