package com.tcss559.alltollpass.repository.traveler;

import com.tcss559.alltollpass.entity.traveler.TravelerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TravelerTransactionRepository extends JpaRepository<TravelerTransaction, Long> {

    List<TravelerTransaction> findByUserId(Long userId);
}
