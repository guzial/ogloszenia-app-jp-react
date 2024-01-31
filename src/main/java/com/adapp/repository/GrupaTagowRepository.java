package com.adapp.repository;

import com.adapp.domain.GrupaTagow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the GrupaTagow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GrupaTagowRepository extends JpaRepository<GrupaTagow, Long> {}
