package mk.ukim.finki.exam.repository;

import mk.ukim.finki.exam.domain.Appuser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Appuser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppuserRepository extends JpaRepository<Appuser, Long>, JpaSpecificationExecutor<Appuser> {

}
