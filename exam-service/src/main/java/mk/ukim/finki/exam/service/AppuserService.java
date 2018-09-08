package mk.ukim.finki.exam.service;

import mk.ukim.finki.exam.domain.Appuser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Appuser.
 */
public interface AppuserService {

    /**
     * Save a appuser.
     *
     * @param appuser the entity to save
     * @return the persisted entity
     */
    Appuser save(Appuser appuser);

    /**
     * Get all the appusers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Appuser> findAll(Pageable pageable);


    /**
     * Get the "id" appuser.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Appuser> findOne(Long id);

    /**
     * Delete the "id" appuser.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
