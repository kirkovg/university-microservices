package mk.ukim.finki.food.service;

import mk.ukim.finki.food.domain.Recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Recipe.
 */
public interface RecipeService {

    /**
     * Save a recipe.
     *
     * @param recipe the entity to save
     * @return the persisted entity
     */
    Recipe save(Recipe recipe);

    /**
     * Get all the recipes.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<Recipe> findAll(Pageable pageable);


    /**
     * Get the "id" recipe.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<Recipe> findOne(Long id);

    /**
     * Delete the "id" recipe.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
