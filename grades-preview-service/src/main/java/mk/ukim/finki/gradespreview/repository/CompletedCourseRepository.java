package mk.ukim.finki.gradespreview.repository;

import mk.ukim.finki.gradespreview.domain.CompletedCourse;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CompletedCourse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompletedCourseRepository extends JpaRepository<CompletedCourse, Long>, JpaSpecificationExecutor<CompletedCourse> {

}
