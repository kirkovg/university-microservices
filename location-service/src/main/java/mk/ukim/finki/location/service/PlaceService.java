package mk.ukim.finki.location.service;

import mk.ukim.finki.location.domain.Place;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Place.
 */
public interface PlaceService {

    /**
     * Save a place.
     *
     * @param place the entity to save
     * @return the persisted entity
     */
    Place save(Place place);

    /**
     * Get all the places.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Place> findAll(Pageable pageable);


    /**
     * Get the "id" place.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Place> findOne(Long id);

    /**
     * Delete the "id" place.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
