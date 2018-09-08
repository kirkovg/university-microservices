package mk.ukim.finki.location.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.location.domain.Place;
import mk.ukim.finki.location.domain.*; // for static metamodels
import mk.ukim.finki.location.repository.PlaceRepository;
import mk.ukim.finki.location.service.dto.PlaceCriteria;


/**
 * Service for executing complex queries for Place entities in the database.
 * The main input is a {@link PlaceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Place} or a {@link Page} of {@link Place} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlaceQueryService extends QueryService<Place> {

    private final Logger log = LoggerFactory.getLogger(PlaceQueryService.class);

    private final PlaceRepository placeRepository;

    public PlaceQueryService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    /**
     * Return a {@link List} of {@link Place} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Place> findByCriteria(PlaceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Place> specification = createSpecification(criteria);
        return placeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Place} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Place> findByCriteria(PlaceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Place> specification = createSpecification(criteria);
        return placeRepository.findAll(specification, page);
    }

    /**
     * Function to convert PlaceCriteria to a {@link Specification}
     */
    private Specification<Place> createSpecification(PlaceCriteria criteria) {
        Specification<Place> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Place_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Place_.name));
            }
            if (criteria.getAddressLine() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddressLine(), Place_.addressLine));
            }
            if (criteria.getPosX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosX(), Place_.posX));
            }
            if (criteria.getPosY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPosY(), Place_.posY));
            }
        }
        return specification;
    }

}
