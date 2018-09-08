package mk.ukim.finki.publictransport.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.publictransport.domain.Line;
import mk.ukim.finki.publictransport.domain.*; // for static metamodels
import mk.ukim.finki.publictransport.repository.LineRepository;
import mk.ukim.finki.publictransport.service.dto.LineCriteria;


/**
 * Service for executing complex queries for Line entities in the database.
 * The main input is a {@link LineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Line} or a {@link Page} of {@link Line} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LineQueryService extends QueryService<Line> {

    private final Logger log = LoggerFactory.getLogger(LineQueryService.class);

    private final LineRepository lineRepository;

    public LineQueryService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    /**
     * Return a {@link List} of {@link Line} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Line> findByCriteria(LineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Line> specification = createSpecification(criteria);
        return lineRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Line} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Line> findByCriteria(LineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Line> specification = createSpecification(criteria);
        return lineRepository.findAll(specification, page);
    }

    /**
     * Function to convert LineCriteria to a {@link Specification}
     */
    private Specification<Line> createSpecification(LineCriteria criteria) {
        Specification<Line> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Line_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Line_.name));
            }
            if (criteria.getUniversityStopTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUniversityStopTime(), Line_.universityStopTime));
            }
        }
        return specification;
    }

}
