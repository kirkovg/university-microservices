package mk.ukim.finki.officehours.service;

import mk.ukim.finki.officehours.domain.Scheduledclass;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Scheduledclass.
 */
public interface ScheduledclassService {

    /**
     * Save a scheduledclass.
     *
     * @param scheduledclass the entity to save
     * @return the persisted entity
     */
    Scheduledclass save(Scheduledclass scheduledclass);

    /**
     * Get all the scheduledclasses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Scheduledclass> findAll(Pageable pageable);


    /**
     * Get the "id" scheduledclass.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Scheduledclass> findOne(Long id);

    /**
     * Delete the "id" scheduledclass.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
