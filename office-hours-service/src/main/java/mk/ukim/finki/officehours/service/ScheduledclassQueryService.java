package mk.ukim.finki.officehours.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.officehours.domain.Scheduledclass;
import mk.ukim.finki.officehours.domain.*; // for static metamodels
import mk.ukim.finki.officehours.repository.ScheduledclassRepository;
import mk.ukim.finki.officehours.service.dto.ScheduledclassCriteria;


/**
 * Service for executing complex queries for Scheduledclass entities in the database.
 * The main input is a {@link ScheduledclassCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Scheduledclass} or a {@link Page} of {@link Scheduledclass} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScheduledclassQueryService extends QueryService<Scheduledclass> {

    private final Logger log = LoggerFactory.getLogger(ScheduledclassQueryService.class);

    private final ScheduledclassRepository scheduledclassRepository;

    public ScheduledclassQueryService(ScheduledclassRepository scheduledclassRepository) {
        this.scheduledclassRepository = scheduledclassRepository;
    }

    /**
     * Return a {@link List} of {@link Scheduledclass} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Scheduledclass> findByCriteria(ScheduledclassCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Scheduledclass> specification = createSpecification(criteria);
        return scheduledclassRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Scheduledclass} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Scheduledclass> findByCriteria(ScheduledclassCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Scheduledclass> specification = createSpecification(criteria);
        return scheduledclassRepository.findAll(specification, page);
    }

    /**
     * Function to convert ScheduledclassCriteria to a {@link Specification}
     */
    private Specification<Scheduledclass> createSpecification(ScheduledclassCriteria criteria) {
        Specification<Scheduledclass> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Scheduledclass_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Scheduledclass_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Scheduledclass_.description));
            }
            if (criteria.getLecturer() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLecturer(), Scheduledclass_.lecturer));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Scheduledclass_.location));
            }
            if (criteria.getDay() != null) {
                specification = specification.and(buildSpecification(criteria.getDay(), Scheduledclass_.day));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Scheduledclass_.date));
            }
        }
        return specification;
    }

}
