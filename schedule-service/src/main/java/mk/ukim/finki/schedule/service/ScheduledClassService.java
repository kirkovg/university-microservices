package mk.ukim.finki.schedule.service;

import mk.ukim.finki.schedule.domain.ScheduledClass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing ScheduledClass.
 */
public interface ScheduledClassService {

    /**
     * Save a scheduledClass.
     *
     * @param scheduledClass the entity to save
     * @return the persisted entity
     */
    ScheduledClass save(ScheduledClass scheduledClass);

    /**
     * Get all the scheduledClasses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<ScheduledClass> findAll(Pageable pageable);


    /**
     * Get the "id" scheduledClass.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ScheduledClass> findOne(Long id);

    /**
     * Delete the "id" scheduledClass.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
