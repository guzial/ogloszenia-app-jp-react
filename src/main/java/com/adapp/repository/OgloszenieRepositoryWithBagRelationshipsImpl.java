package com.adapp.repository;

import com.adapp.domain.Ogloszenie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class OgloszenieRepositoryWithBagRelationshipsImpl implements OgloszenieRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Ogloszenie> fetchBagRelationships(Optional<Ogloszenie> ogloszenie) {
        return ogloszenie.map(this::fetchTags);
    }

    @Override
    public Page<Ogloszenie> fetchBagRelationships(Page<Ogloszenie> ogloszenies) {
        return new PageImpl<>(fetchBagRelationships(ogloszenies.getContent()), ogloszenies.getPageable(), ogloszenies.getTotalElements());
    }

    @Override
    public List<Ogloszenie> fetchBagRelationships(List<Ogloszenie> ogloszenies) {
        return Optional.of(ogloszenies).map(this::fetchTags).orElse(Collections.emptyList());
    }

    Ogloszenie fetchTags(Ogloszenie result) {
        return entityManager
            .createQuery(
                "select ogloszenie from Ogloszenie ogloszenie left join fetch ogloszenie.tags where ogloszenie.id = :id",
                Ogloszenie.class
            )
            .setParameter("id", result.getId())
            .getSingleResult();
    }

    List<Ogloszenie> fetchTags(List<Ogloszenie> ogloszenies) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, ogloszenies.size()).forEach(index -> order.put(ogloszenies.get(index).getId(), index));
        List<Ogloszenie> result = entityManager
            .createQuery(
                "select ogloszenie from Ogloszenie ogloszenie left join fetch ogloszenie.tags where ogloszenie in :ogloszenies",
                Ogloszenie.class
            )
            .setParameter("ogloszenies", ogloszenies)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
