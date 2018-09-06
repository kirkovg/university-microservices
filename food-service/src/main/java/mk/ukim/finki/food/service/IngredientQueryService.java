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

import mk.ukim.finki.food.domain.Ingredient;
import mk.ukim.finki.food.domain.*; // for static metamodels
import mk.ukim.finki.food.repository.IngredientRepository;
import mk.ukim.finki.food.service.dto.IngredientCriteria;


/**
 * Service for executing complex queries for Ingredient entities in the database.
 * The main input is a {@link IngredientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ingredient} or a {@link Page} of {@link Ingredient} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IngredientQueryService extends QueryService<Ingredient> {

    private final Logger log = LoggerFactory.getLogger(IngredientQueryService.class);

    private final IngredientRepository ingredientRepository;

    public IngredientQueryService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    /**
     * Return a {@link List} of {@link Ingredient} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ingredient> findByCriteria(IngredientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ingredient> specification = createSpecification(criteria);
        return ingredientRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ingredient} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ingredient> findByCriteria(IngredientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ingredient> specification = createSpecification(criteria);
        return ingredientRepository.findAll(specification, page);
    }

    /**
     * Function to convert IngredientCriteria to a {@link Specification}
     */
    private Specification<Ingredient> createSpecification(IngredientCriteria criteria) {
        Specification<Ingredient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Ingredient_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Ingredient_.name));
            }
            if (criteria.getWeightInGrams() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeightInGrams(), Ingredient_.weightInGrams));
            }
        }
        return specification;
    }

}
