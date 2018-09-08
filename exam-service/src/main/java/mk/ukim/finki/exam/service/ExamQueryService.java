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

import mk.ukim.finki.exam.domain.Exam;
import mk.ukim.finki.exam.domain.*; // for static metamodels
import mk.ukim.finki.exam.repository.ExamRepository;
import mk.ukim.finki.exam.service.dto.ExamCriteria;


/**
 * Service for executing complex queries for Exam entities in the database.
 * The main input is a {@link ExamCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Exam} or a {@link Page} of {@link Exam} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExamQueryService extends QueryService<Exam> {

    private final Logger log = LoggerFactory.getLogger(ExamQueryService.class);

    private final ExamRepository examRepository;

    public ExamQueryService(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    /**
     * Return a {@link List} of {@link Exam} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Exam> findByCriteria(ExamCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Exam> specification = createSpecification(criteria);
        return examRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Exam} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Exam> findByCriteria(ExamCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Exam> specification = createSpecification(criteria);
        return examRepository.findAll(specification, page);
    }

    /**
     * Function to convert ExamCriteria to a {@link Specification}
     */
    private Specification<Exam> createSpecification(ExamCriteria criteria) {
        Specification<Exam> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Exam_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Exam_.name));
            }
            if (criteria.getLocation() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocation(), Exam_.location));
            }
            if (criteria.getTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTime(), Exam_.time));
            }
        }
        return specification;
    }

}
