package mk.ukim.finki.food.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.food.domain.Restaurant;
import mk.ukim.finki.food.domain.*; // for static metamodels
import mk.ukim.finki.food.repository.RestaurantRepository;
import mk.ukim.finki.food.service.dto.RestaurantCriteria;


/**
 * Service for executing complex queries for Restaurant entities in the database.
 * The main input is a {@link RestaurantCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Restaurant} or a {@link Page} of {@link Restaurant} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RestaurantQueryService extends QueryService<Restaurant> {

    private final Logger log = LoggerFactory.getLogger(RestaurantQueryService.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantQueryService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Return a {@link List} of {@link Restaurant} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Restaurant> findByCriteria(RestaurantCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Restaurant> specification = createSpecification(criteria);
        return restaurantRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Restaurant} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Restaurant> findByCriteria(RestaurantCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Restaurant> specification = createSpecification(criteria);
        return restaurantRepository.findAll(specification, page);
    }

    /**
     * Function to convert RestaurantCriteria to a {@link Specification}
     */
    private Specification<Restaurant> createSpecification(RestaurantCriteria criteria) {
        Specification<Restaurant> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Restaurant_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Restaurant_.name));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Restaurant_.location));
            }
            if (criteria.getCapacity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacity(), Restaurant_.capacity));
            }
            if (criteria.getMenuId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getMenuId(), Restaurant_.menus, Menu_.id));
            }
        }
        return specification;
    }

}
