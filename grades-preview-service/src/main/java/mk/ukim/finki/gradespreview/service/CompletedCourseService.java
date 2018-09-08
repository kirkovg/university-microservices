package mk.ukim.finki.gradespreview.service;

import mk.ukim.finki.gradespreview.domain.CompletedCourse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CompletedCourse.
 */
public interface CompletedCourseService {

    /**
     * Save a completedCourse.
     *
     * @param completedCourse the entity to save
     * @return the persisted entity
     */
    CompletedCourse save(CompletedCourse completedCourse);

    /**
     * Get all the completedCourses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CompletedCourse> findAll(Pageable pageable);


    /**
     * Get the "id" completedCourse.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CompletedCourse> findOne(Long id);

    /**
     * Delete the "id" completedCourse.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
