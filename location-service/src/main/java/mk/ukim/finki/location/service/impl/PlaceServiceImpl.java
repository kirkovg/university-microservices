package mk.ukim.finki.location.service.impl;

import mk.ukim.finki.location.service.PlaceService;
import mk.ukim.finki.location.domain.Place;
import mk.ukim.finki.location.repository.PlaceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Place.
 */
@Service
@Transactional
public class PlaceServiceImpl implements PlaceService {

    private final Logger log = LoggerFactory.getLogger(PlaceServiceImpl.class);

    private final PlaceRepository placeRepository;

    public PlaceServiceImpl(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    /**
     * Save a place.
     *
     * @param place the entity to save
     * @return the persisted entity
     */
    @Override
    public Place save(Place place) {
        log.debug("Request to save Place : {}", place);        return placeRepository.save(place);
    }

    /**
     * Get all the places.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Place> findAll(Pageable pageable) {
        log.debug("Request to get all Places");
        return placeRepository.findAll(pageable);
    }


    /**
     * Get one place by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Place> findOne(Long id) {
        log.debug("Request to get Place : {}", id);
        return placeRepository.findById(id);
    }

    /**
     * Delete the place by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Place : {}", id);
        placeRepository.deleteById(id);
    }
}
