package mk.ukim.finki.schedule.repository;

import mk.ukim.finki.schedule.domain.ScheduledClass;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ScheduledClass entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledClassRepository extends JpaRepository<ScheduledClass, Long>, JpaSpecificationExecutor<ScheduledClass> {

}
