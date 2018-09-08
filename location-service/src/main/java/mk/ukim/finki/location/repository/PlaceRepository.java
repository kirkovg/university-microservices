package mk.ukim.finki.location.repository;

import mk.ukim.finki.location.domain.Place;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Place entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlaceRepository extends JpaRepository<Place, Long>, JpaSpecificationExecutor<Place> {

}
