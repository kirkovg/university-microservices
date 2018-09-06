package mk.ukim.finki.sport.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.sport.domain.Player;
import mk.ukim.finki.sport.domain.*; // for static metamodels
import mk.ukim.finki.sport.repository.PlayerRepository;
import mk.ukim.finki.sport.service.dto.PlayerCriteria;


/**
 * Service for executing complex queries for Player entities in the database.
 * The main input is a {@link PlayerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Player} or a {@link Page} of {@link Player} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlayerQueryService extends QueryService<Player> {

    private final Logger log = LoggerFactory.getLogger(PlayerQueryService.class);

    private final PlayerRepository playerRepository;

    public PlayerQueryService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Return a {@link List} of {@link Player} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Player> findByCriteria(PlayerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Player> specification = createSpecification(criteria);
        return playerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Player} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Player> findByCriteria(PlayerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Player> specification = createSpecification(criteria);
        return playerRepository.findAll(specification, page);
    }

    /**
     * Function to convert PlayerCriteria to a {@link Specification}
     */
    private Specification<Player> createSpecification(PlayerCriteria criteria) {
        Specification<Player> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Player_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Player_.name));
            }
            if (criteria.getAge() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAge(), Player_.age));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), Player_.weight));
            }
            if (criteria.getHeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHeight(), Player_.height));
            }
        }
        return specification;
    }

}
