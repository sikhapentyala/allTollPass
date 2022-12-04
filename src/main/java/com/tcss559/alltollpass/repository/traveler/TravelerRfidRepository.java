package com.tcss559.alltollpass.repository.traveler;

import com.tcss559.alltollpass.entity.traveler.TravelerRfid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelerRfidRepository extends JpaRepository<TravelerRfid, Long> {
    Optional<TravelerRfid> findByRfid(String rfid);
    List<TravelerRfid> findByUserId(Long userId);
    long deleteByRfid(String rfid);
}
