package mk.ukim.finki.exam.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.exam.domain.Appuser;
import mk.ukim.finki.exam.domain.*; // for static metamodels
import mk.ukim.finki.exam.repository.AppuserRepository;
import mk.ukim.finki.exam.service.dto.AppuserCriteria;


/**
 * Service for executing complex queries for Appuser entities in the database.
 * The main input is a {@link AppuserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Appuser} or a {@link Page} of {@link Appuser} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppuserQueryService extends QueryService<Appuser> {

    private final Logger log = LoggerFactory.getLogger(AppuserQueryService.class);

    private final AppuserRepository appuserRepository;

    public AppuserQueryService(AppuserRepository appuserRepository) {
        this.appuserRepository = appuserRepository;
    }

    /**
     * Return a {@link List} of {@link Appuser} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Appuser> findByCriteria(AppuserCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Appuser> specification = createSpecification(criteria);
        return appuserRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Appuser} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Appuser> findByCriteria(AppuserCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Appuser> specification = createSpecification(criteria);
        return appuserRepository.findAll(specification, page);
    }

    /**
     * Function to convert AppuserCriteria to a {@link Specification}
     */
    private Specification<Appuser> createSpecification(AppuserCriteria criteria) {
        Specification<Appuser> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Appuser_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Appuser_.name));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Appuser_.email));
            }
            if (criteria.getExamId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getExamId(), Appuser_.exams, Exam_.id));
            }
        }
        return specification;
    }

}
