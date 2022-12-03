package com.tcss559.alltollpass.repository;

import com.tcss559.alltollpass.entity.user.UserTransactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserTransactionsRepository extends JpaRepository<UserTransactions, Long> {

    List<UserTransactions> findByUserId(Long userId);
}
