package com.adapp.repository;

import com.adapp.domain.Seniority;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Seniority entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeniorityRepository extends JpaRepository<Seniority, Long> {}
