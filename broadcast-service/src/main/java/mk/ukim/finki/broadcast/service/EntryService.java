package mk.ukim.finki.broadcast.service;

import mk.ukim.finki.broadcast.domain.Entry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Entry.
 */
public interface EntryService {

    /**
     * Save a entry.
     *
     * @param entry the entity to save
     * @return the persisted entity
     */
    Entry save(Entry entry);

    /**
     * Get all the entries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Entry> findAll(Pageable pageable);


    /**
     * Get the "id" entry.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Entry> findOne(Long id);

    /**
     * Delete the "id" entry.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
