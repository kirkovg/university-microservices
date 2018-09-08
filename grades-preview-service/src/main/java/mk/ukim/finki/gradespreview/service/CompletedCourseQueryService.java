package mk.ukim.finki.gradespreview.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.gradespreview.domain.CompletedCourse;
import mk.ukim.finki.gradespreview.domain.*; // for static metamodels
import mk.ukim.finki.gradespreview.repository.CompletedCourseRepository;
import mk.ukim.finki.gradespreview.service.dto.CompletedCourseCriteria;


/**
 * Service for executing complex queries for CompletedCourse entities in the database.
 * The main input is a {@link CompletedCourseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompletedCourse} or a {@link Page} of {@link CompletedCourse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompletedCourseQueryService extends QueryService<CompletedCourse> {

    private final Logger log = LoggerFactory.getLogger(CompletedCourseQueryService.class);

    private final CompletedCourseRepository completedCourseRepository;

    public CompletedCourseQueryService(CompletedCourseRepository completedCourseRepository) {
        this.completedCourseRepository = completedCourseRepository;
    }

    /**
     * Return a {@link List} of {@link CompletedCourse} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompletedCourse> findByCriteria(CompletedCourseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CompletedCourse> specification = createSpecification(criteria);
        return completedCourseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link CompletedCourse} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompletedCourse> findByCriteria(CompletedCourseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CompletedCourse> specification = createSpecification(criteria);
        return completedCourseRepository.findAll(specification, page);
    }

    /**
     * Function to convert CompletedCourseCriteria to a {@link Specification}
     */
    private Specification<CompletedCourse> createSpecification(CompletedCourseCriteria criteria) {
        Specification<CompletedCourse> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), CompletedCourse_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), CompletedCourse_.name));
            }
            if (criteria.getGrade() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrade(), CompletedCourse_.grade));
            }
        }
        return specification;
    }

}
