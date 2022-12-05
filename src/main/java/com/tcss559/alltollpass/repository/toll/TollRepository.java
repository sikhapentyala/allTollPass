package com.tcss559.alltollpass.repository.toll;

import com.tcss559.alltollpass.entity.toll.Toll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TollRepository extends JpaRepository<Toll, Long> {
    Optional<Toll> findByIdAndUserId(Long id, Long userId);
}
