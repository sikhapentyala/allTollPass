package com.tcss559.alltollpass.repository;

import com.tcss559.alltollpass.entity.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationRepository extends JpaRepository<Authentication, Long> {

}
