package mk.ukim.finki.photo.service;

import mk.ukim.finki.photo.domain.Photo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Photo.
 */
public interface PhotoService {

    /**
     * Save a photo.
     *
     * @param photo the entity to save
     * @return the persisted entity
     */
    Photo save(Photo photo);

    /**
     * Get all the photos.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Photo> findAll(Pageable pageable);


    /**
     * Get the "id" photo.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Photo> findOne(Long id);

    /**
     * Delete the "id" photo.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
