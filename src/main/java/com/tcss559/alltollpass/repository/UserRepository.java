package com.tcss559.alltollpass.repository;

import com.tcss559.alltollpass.entity.User;
import com.tcss559.alltollpass.entity.traveler.TravelerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(String name);
    Optional<User> findByEmail(String email);
}
