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

import mk.ukim.finki.food.domain.Recipe;
import mk.ukim.finki.food.domain.*; // for static metamodels
import mk.ukim.finki.food.repository.RecipeRepository;
import mk.ukim.finki.food.service.dto.RecipeCriteria;


/**
 * Service for executing complex queries for Recipe entities in the database.
 * The main input is a {@link RecipeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Recipe} or a {@link Page} of {@link Recipe} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RecipeQueryService extends QueryService<Recipe> {

    private final Logger log = LoggerFactory.getLogger(RecipeQueryService.class);

    private final RecipeRepository recipeRepository;

    public RecipeQueryService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    /**
     * Return a {@link List} of {@link Recipe} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Recipe> findByCriteria(RecipeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Recipe> specification = createSpecification(criteria);
        return recipeRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Recipe} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Recipe> findByCriteria(RecipeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Recipe> specification = createSpecification(criteria);
        return recipeRepository.findAll(specification, page);
    }

    /**
     * Function to convert RecipeCriteria to a {@link Specification}
     */
    private Specification<Recipe> createSpecification(RecipeCriteria criteria) {
        Specification<Recipe> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Recipe_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Recipe_.name));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), Recipe_.price));
            }
            if (criteria.getIngredientId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getIngredientId(), Recipe_.ingredients, Ingredient_.id));
            }
        }
        return specification;
    }

}
