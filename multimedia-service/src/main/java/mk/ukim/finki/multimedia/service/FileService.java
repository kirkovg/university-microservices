package mk.ukim.finki.multimedia.service;

import mk.ukim.finki.multimedia.domain.File;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing File.
 */
public interface FileService {

    /**
     * Save a file.
     *
     * @param file the entity to save
     * @return the persisted entity
     */
    File save(File file);

    /**
     * Get all the files.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<File> findAll(Pageable pageable);


    /**
     * Get the "id" file.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<File> findOne(Long id);

    /**
     * Delete the "id" file.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
