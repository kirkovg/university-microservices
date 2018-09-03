package mk.ukim.finki.emergency.repository;

import mk.ukim.finki.emergency.domain.Entry;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Entry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntryRepository extends JpaRepository<Entry, Long>, JpaSpecificationExecutor<Entry> {

}
