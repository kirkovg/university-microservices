package mk.ukim.finki.gradespreview.web.rest;

import com.codahale.metrics.annotation.Timed;
import mk.ukim.finki.gradespreview.domain.CompletedCourse;
import mk.ukim.finki.gradespreview.service.CompletedCourseService;
import mk.ukim.finki.gradespreview.web.rest.errors.BadRequestAlertException;
import mk.ukim.finki.gradespreview.web.rest.util.HeaderUtil;
import mk.ukim.finki.gradespreview.web.rest.util.PaginationUtil;
import mk.ukim.finki.gradespreview.service.dto.CompletedCourseCriteria;
import mk.ukim.finki.gradespreview.service.CompletedCourseQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CompletedCourse.
 */
@RestController
@RequestMapping("/api")
public class CompletedCourseResource {

    private final Logger log = LoggerFactory.getLogger(CompletedCourseResource.class);

    private static final String ENTITY_NAME = "gradespreviewCompletedCourse";

    private final CompletedCourseService completedCourseService;

    private final CompletedCourseQueryService completedCourseQueryService;

    public CompletedCourseResource(CompletedCourseService completedCourseService, CompletedCourseQueryService completedCourseQueryService) {
        this.completedCourseService = completedCourseService;
        this.completedCourseQueryService = completedCourseQueryService;
    }

    /**
     * POST  /completed-courses : Create a new completedCourse.
     *
     * @param completedCourse the completedCourse to create
     * @return the ResponseEntity with status 201 (Created) and with body the new completedCourse, or with status 400 (Bad Request) if the completedCourse has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/completed-courses")
    @Timed
    public ResponseEntity<CompletedCourse> createCompletedCourse(@RequestBody CompletedCourse completedCourse) throws URISyntaxException {
        log.debug("REST request to save CompletedCourse : {}", completedCourse);
        if (completedCourse.getId() != null) {
            throw new BadRequestAlertException("A new completedCourse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompletedCourse result = completedCourseService.save(completedCourse);
        return ResponseEntity.created(new URI("/api/completed-courses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /completed-courses : Updates an existing completedCourse.
     *
     * @param completedCourse the completedCourse to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated completedCourse,
     * or with status 400 (Bad Request) if the completedCourse is not valid,
     * or with status 500 (Internal Server Error) if the completedCourse couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/completed-courses")
    @Timed
    public ResponseEntity<CompletedCourse> updateCompletedCourse(@RequestBody CompletedCourse completedCourse) throws URISyntaxException {
        log.debug("REST request to update CompletedCourse : {}", completedCourse);
        if (completedCourse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CompletedCourse result = completedCourseService.save(completedCourse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, completedCourse.getId().toString()))
            .body(result);
    }

    /**
     * GET  /completed-courses : get all the completedCourses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of completedCourses in body
     */
    @GetMapping("/completed-courses")
    @Timed
    public ResponseEntity<List<CompletedCourse>> getAllCompletedCourses(CompletedCourseCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CompletedCourses by criteria: {}", criteria);
        Page<CompletedCourse> page = completedCourseQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/completed-courses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /completed-courses/:id : get the "id" completedCourse.
     *
     * @param id the id of the completedCourse to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the completedCourse, or with status 404 (Not Found)
     */
    @GetMapping("/completed-courses/{id}")
    @Timed
    public ResponseEntity<CompletedCourse> getCompletedCourse(@PathVariable Long id) {
        log.debug("REST request to get CompletedCourse : {}", id);
        Optional<CompletedCourse> completedCourse = completedCourseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(completedCourse);
    }

    /**
     * DELETE  /completed-courses/:id : delete the "id" completedCourse.
     *
     * @param id the id of the completedCourse to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/completed-courses/{id}")
    @Timed
    public ResponseEntity<Void> deleteCompletedCourse(@PathVariable Long id) {
        log.debug("REST request to delete CompletedCourse : {}", id);
        completedCourseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
