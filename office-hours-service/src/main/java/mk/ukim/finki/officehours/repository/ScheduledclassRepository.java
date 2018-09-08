package mk.ukim.finki.officehours.repository;

import mk.ukim.finki.officehours.domain.Scheduledclass;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Scheduledclass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledclassRepository extends JpaRepository<Scheduledclass, Long>, JpaSpecificationExecutor<Scheduledclass> {

}
