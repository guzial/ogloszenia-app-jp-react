package com.adapp.repository;

import com.adapp.domain.Ogloszenie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Ogloszenie entity.
 *
 * When extending this class, extend OgloszenieRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface OgloszenieRepository extends OgloszenieRepositoryWithBagRelationships, JpaRepository<Ogloszenie, Long> {
    default Optional<Ogloszenie> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Ogloszenie> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Ogloszenie> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select ogloszenie from Ogloszenie ogloszenie left join fetch ogloszenie.seniority left join fetch ogloszenie.typUmowy left join fetch ogloszenie.wystawca",
        countQuery = "select count(ogloszenie) from Ogloszenie ogloszenie"
    )
    Page<Ogloszenie> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select ogloszenie from Ogloszenie ogloszenie left join fetch ogloszenie.seniority left join fetch ogloszenie.typUmowy left join fetch ogloszenie.wystawca"
    )
    List<Ogloszenie> findAllWithToOneRelationships();

    @Query(
        "select ogloszenie from Ogloszenie ogloszenie left join fetch ogloszenie.seniority left join fetch ogloszenie.typUmowy left join fetch ogloszenie.wystawca where ogloszenie.id =:id"
    )
    Optional<Ogloszenie> findOneWithToOneRelationships(@Param("id") Long id);
}
