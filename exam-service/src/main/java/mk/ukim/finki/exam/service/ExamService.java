package mk.ukim.finki.exam.service;

import mk.ukim.finki.exam.domain.Exam;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Exam.
 */
public interface ExamService {

    /**
     * Save a exam.
     *
     * @param exam the entity to save
     * @return the persisted entity
     */
    Exam save(Exam exam);

    /**
     * Get all the exams.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Exam> findAll(Pageable pageable);


    /**
     * Get the "id" exam.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Exam> findOne(Long id);

    /**
     * Delete the "id" exam.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
