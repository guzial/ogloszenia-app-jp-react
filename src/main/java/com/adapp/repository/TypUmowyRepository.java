package com.adapp.repository;

import com.adapp.domain.TypUmowy;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TypUmowy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypUmowyRepository extends JpaRepository<TypUmowy, Long> {}
