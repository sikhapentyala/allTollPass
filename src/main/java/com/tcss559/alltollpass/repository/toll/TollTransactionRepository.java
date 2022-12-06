package com.tcss559.alltollpass.repository.toll;

import com.tcss559.alltollpass.entity.toll.TollTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TollTransactionRepository extends JpaRepository<TollTransaction, Long> {
    List<TollTransaction> findByAgencyId(Long agencyId);

    Optional<TollTransaction> findByAgencyIdAndTollTransactionId(Long agencyId, String transactionId);
}
