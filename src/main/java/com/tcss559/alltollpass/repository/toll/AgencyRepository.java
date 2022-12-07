package com.tcss559.alltollpass.repository.toll;

import com.tcss559.alltollpass.entity.toll.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {

}
