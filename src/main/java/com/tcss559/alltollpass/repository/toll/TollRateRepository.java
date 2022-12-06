package com.tcss559.alltollpass.repository.toll;

import com.tcss559.alltollpass.entity.toll.TollRate;
import com.tcss559.alltollpass.entity.traveler.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TollRateRepository extends JpaRepository<TollRate, Long> {
    List<TollRate> findByAgencyId(Long agencyId);
    Optional<TollRate> findByAgencyIdAndVehicleType(Long agencyId, VehicleType vehicleType);

    long deleteByAgencyIdAndVehicleType(Long agencyId, VehicleType vehicleType);
}
