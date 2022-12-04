package com.tcss559.alltollpass.repository;

import com.tcss559.alltollpass.entity.user.UserRfid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRfidRepository extends JpaRepository<UserRfid, Long> {
    Optional<UserRfid> findByRfid(String rfid);
    List<UserRfid> findByUserId(Long userId);

    long deleteByRfid(String rfid);
}
