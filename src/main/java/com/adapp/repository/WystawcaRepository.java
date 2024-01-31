package com.adapp.repository;

import com.adapp.domain.Wystawca;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Wystawca entity.
 */
@Repository
public interface WystawcaRepository extends JpaRepository<Wystawca, Long> {
    @Query("select wystawca from Wystawca wystawca where wystawca.user.login = ?#{authentication.name}")
    List<Wystawca> findByUserIsCurrentUser();

    default Optional<Wystawca> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Wystawca> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Wystawca> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select wystawca from Wystawca wystawca left join fetch wystawca.user",
        countQuery = "select count(wystawca) from Wystawca wystawca"
    )
    Page<Wystawca> findAllWithToOneRelationships(Pageable pageable);

    @Query("select wystawca from Wystawca wystawca left join fetch wystawca.user")
    List<Wystawca> findAllWithToOneRelationships();

    @Query("select wystawca from Wystawca wystawca left join fetch wystawca.user where wystawca.id =:id")
    Optional<Wystawca> findOneWithToOneRelationships(@Param("id") Long id);
}
