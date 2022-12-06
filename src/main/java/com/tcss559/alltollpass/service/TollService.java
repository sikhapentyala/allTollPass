package com.tcss559.alltollpass.service;

import com.tcss559.alltollpass.config.AppConfig;
import com.tcss559.alltollpass.entity.toll.Agency;
import com.tcss559.alltollpass.entity.toll.TollRate;
import com.tcss559.alltollpass.entity.toll.TollTransaction;
import com.tcss559.alltollpass.entity.toll.TransactionStatus;
import com.tcss559.alltollpass.exception.DatabaseException;
import com.tcss559.alltollpass.exception.TollNotFoundException;
import com.tcss559.alltollpass.model.request.toll.LocationRequest;
import com.tcss559.alltollpass.model.request.toll.TollRateRequest;
import com.tcss559.alltollpass.model.request.toll.TollTransactionRequest;
import com.tcss559.alltollpass.model.response.toll.LocationResponse;
import com.tcss559.alltollpass.model.response.toll.TollRateDetail;
import com.tcss559.alltollpass.model.response.toll.TollRateResponse;
import com.tcss559.alltollpass.model.response.toll.TransactionResponse;
import com.tcss559.alltollpass.repository.toll.TollRateRepository;
import com.tcss559.alltollpass.repository.toll.AgencyRepository;
import com.tcss559.alltollpass.repository.toll.TollTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TollService {

    @Autowired
    AppConfig appConfig;
    @Autowired
    AgencyRepository agencyRepository;

    @Autowired
    TollRateRepository tollRateRepository;

    @Autowired
    TollTransactionRepository tollTransactionRepository;

    @Autowired
    UserService userService;


    public LocationResponse updateLocation(LocationRequest locationRequest) {
        Agency agency = agencyRepository.save(Agency.builder()
                .location(locationRequest.getLocation())
                .id(locationRequest.getAgencyId())
                .build()
        );

        return LocationResponse.builder()
                .agencyId(agency.getId())
                .location(agency.getLocation())
                .build();
    }

    public TollRateResponse upsertTollRates(TollRateRequest tollRateRequest) {
        Agency agency = agencyRepository.findById(tollRateRequest.getAgencyId()).orElseThrow(() -> new TollNotFoundException("Toll not found"));
        Optional<TollRate> optionalRate = tollRateRepository.findByAgencyIdAndVehicleType(agency.getId(), tollRateRequest.getVehicleType());
        TollRate rate;
        if(optionalRate.isPresent()){
            rate = optionalRate.get();
            rate.setTollRate(tollRateRequest.getTollRate());
            rate.setUpdatedTimestamp(LocalDateTime.now());

        }else{
            rate = TollRate.builder()
                    .agencyId(agency.getId())
                    .tollRate(tollRateRequest.getTollRate())
                    .vehicleType(tollRateRequest.getVehicleType())
                    .build();
        }
        try{
            TollRate savedRate = tollRateRepository.save(rate);
            List<TollRate> rates = tollRateRepository.findByAgencyId(tollRateRequest.getAgencyId());
            return TollRateResponse.builder()
                    .agencyId(savedRate.getAgencyId())
                    .location(agency.getLocation())
                    .rates(rates.stream()
                            .map(it -> TollRateDetail.builder()
                                    .tollRate(it.getTollRate())
                                    .vehicleType(it.getVehicleType())
                                    .build()
                            )
                            .collect(Collectors.toList())
                    )
                    .build();
        }catch (RuntimeException e){
            throw new DatabaseException(e);
        }
    }

    public TollRateResponse getTollRates(Long agencyId) {
        Agency agency = agencyRepository.findById(agencyId).orElseThrow(() -> new TollNotFoundException("Toll not found"));
        List<TollRate> rates = tollRateRepository.findByAgencyId(agency.getId());
        return TollRateResponse.builder()
                .agencyId(agency.getId())
                .location(agency.getLocation())
                .rates(rates.stream()
                        .map(it -> TollRateDetail.builder()
                                .tollRate(it.getTollRate())
                                .vehicleType(it.getVehicleType())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();

    }

    public TollRateResponse deleteTollRate(TollRateRequest tollRateRequest) {
        Agency agency = agencyRepository.findById(tollRateRequest.getAgencyId()).orElseThrow(() -> new TollNotFoundException("Toll not found"));
        tollRateRepository.deleteByAgencyIdAndVehicleType(agency.getId(), tollRateRequest.getVehicleType());

        List<TollRate> rates = tollRateRepository.findByAgencyId(agency.getId());
        return TollRateResponse.builder()
                .agencyId(agency.getId())
                .location(agency.getLocation())
                .rates(rates.stream()
                        .map(it -> TollRateDetail.builder()
                                .tollRate(it.getTollRate())
                                .vehicleType(it.getVehicleType())
                                .build()
                        )
                        .collect(Collectors.toList())
                )
                .build();
    }


    public List<TransactionResponse> getTransactionReportForAgency(Long agencyId) {
        Agency agency = agencyRepository.findById(agencyId).orElseThrow(() -> new TollNotFoundException("No Toll found"));
        List<TollTransaction> transactions = tollTransactionRepository.findByAgencyId(agency.getId());
        return transactions.stream().map(it ->
                TransactionResponse.builder()
                        .tollTransactionId(it.getTollTransactionId())
                        .status(it.getStatus())
                        .rfid(it.getRfid())
                        .createTimestamp(it.getCreateTimestamp())
                        .updateTimestamp(it.getUpdateTimestamp())
                        .agencyId(it.getAgencyId())
                        .build())
                .collect(Collectors.toList());
    }

    public TransactionResponse getTransactionStatusByAgencyId(String transactionId, Long agencyId) {
        Agency agency = agencyRepository.findById(agencyId).orElseThrow(() -> new TollNotFoundException("No Toll found"));
        TollTransaction tollTransaction = tollTransactionRepository.findByAgencyIdAndTollTransactionId(agency.getId(), transactionId).orElseThrow(() -> new TollNotFoundException("No TransactionId Found"));

        return TransactionResponse.builder()
                .agencyId(tollTransaction.getAgencyId())
                .status(tollTransaction.getStatus())
                .tollTransactionId(tollTransaction.getTollTransactionId())
                .build();

    }


    public TransactionStatus createTollTransaction(TollTransactionRequest request) {

        TollTransaction transaction = TollTransaction.builder()
                .rfid(request.getRfid())
                .status(request.getStatus())
                .tollTransactionId(request.getTollTransactionId())
                .agencyId(request.getAgencyId())
                .build();

        return tollTransactionRepository.save(transaction).getStatus();
    }
}
