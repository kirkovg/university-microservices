package mk.ukim.finki.broadcast.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.broadcast.domain.Entry;
import mk.ukim.finki.broadcast.domain.*; // for static metamodels
import mk.ukim.finki.broadcast.repository.EntryRepository;
import mk.ukim.finki.broadcast.service.dto.EntryCriteria;


/**
 * Service for executing complex queries for Entry entities in the database.
 * The main input is a {@link EntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Entry} or a {@link Page} of {@link Entry} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntryQueryService extends QueryService<Entry> {

    private final Logger log = LoggerFactory.getLogger(EntryQueryService.class);

    private final EntryRepository entryRepository;

    public EntryQueryService(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    /**
     * Return a {@link List} of {@link Entry} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Entry> findByCriteria(EntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Entry> specification = createSpecification(criteria);
        return entryRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Entry} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Entry> findByCriteria(EntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Entry> specification = createSpecification(criteria);
        return entryRepository.findAll(specification, page);
    }

    /**
     * Function to convert EntryCriteria to a {@link Specification}
     */
    private Specification<Entry> createSpecification(EntryCriteria criteria) {
        Specification<Entry> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Entry_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Entry_.name));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), Entry_.url));
            }
        }
        return specification;
    }

}
