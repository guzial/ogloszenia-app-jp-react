package com.adapp.repository;

import com.adapp.domain.Ogloszenie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface OgloszenieRepositoryWithBagRelationships {
    Optional<Ogloszenie> fetchBagRelationships(Optional<Ogloszenie> ogloszenie);

    List<Ogloszenie> fetchBagRelationships(List<Ogloszenie> ogloszenies);

    Page<Ogloszenie> fetchBagRelationships(Page<Ogloszenie> ogloszenies);
}
