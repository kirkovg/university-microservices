package mk.ukim.finki.schedule.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.schedule.domain.ScheduledClass;
import mk.ukim.finki.schedule.domain.*; // for static metamodels
import mk.ukim.finki.schedule.repository.ScheduledClassRepository;
import mk.ukim.finki.schedule.service.dto.ScheduledClassCriteria;


/**
 * Service for executing complex queries for ScheduledClass entities in the database.
 * The main input is a {@link ScheduledClassCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ScheduledClass} or a {@link Page} of {@link ScheduledClass} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScheduledClassQueryService extends QueryService<ScheduledClass> {

    private final Logger log = LoggerFactory.getLogger(ScheduledClassQueryService.class);

    private final ScheduledClassRepository scheduledClassRepository;

    public ScheduledClassQueryService(ScheduledClassRepository scheduledClassRepository) {
        this.scheduledClassRepository = scheduledClassRepository;
    }

    /**
     * Return a {@link List} of {@link ScheduledClass} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ScheduledClass> findByCriteria(ScheduledClassCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ScheduledClass> specification = createSpecification(criteria);
        return scheduledClassRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link ScheduledClass} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduledClass> findByCriteria(ScheduledClassCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ScheduledClass> specification = createSpecification(criteria);
        return scheduledClassRepository.findAll(specification, page);
    }

    /**
     * Function to convert ScheduledClassCriteria to a {@link Specification}
     */
    private Specification<ScheduledClass> createSpecification(ScheduledClassCriteria criteria) {
        Specification<ScheduledClass> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ScheduledClass_.id));
            }
            if (criteria.getCourseName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCourseName(), ScheduledClass_.courseName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ScheduledClass_.description));
            }
            if (criteria.getLecturer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLecturer(), ScheduledClass_.lecturer));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), ScheduledClass_.location));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), ScheduledClass_.date));
            }
        }
        return specification;
    }

}
