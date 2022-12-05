package com.tcss559.alltollpass.repository.toll;

import com.tcss559.alltollpass.entity.toll.TollTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TollTransactionRepository extends JpaRepository<TollTransaction, Long> {
}
