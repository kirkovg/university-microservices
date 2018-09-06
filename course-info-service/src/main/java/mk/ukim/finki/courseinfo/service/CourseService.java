package mk.ukim.finki.courseinfo.service;

import mk.ukim.finki.courseinfo.domain.Course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Course.
 */
public interface CourseService {

    /**
     * Save a course.
     *
     * @param course the entity to save
     * @return the persisted entity
     */
    Course save(Course course);

    /**
     * Get all the courses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Course> findAll(Pageable pageable);


    /**
     * Get the "id" course.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Course> findOne(Long id);

    /**
     * Delete the "id" course.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
