package mk.ukim.finki.exam.web.rest;

import com.codahale.metrics.annotation.Timed;
import mk.ukim.finki.exam.domain.Exam;
import mk.ukim.finki.exam.service.ExamService;
import mk.ukim.finki.exam.web.rest.errors.BadRequestAlertException;
import mk.ukim.finki.exam.web.rest.util.HeaderUtil;
import mk.ukim.finki.exam.web.rest.util.PaginationUtil;
import mk.ukim.finki.exam.service.dto.ExamCriteria;
import mk.ukim.finki.exam.service.ExamQueryService;
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
 * REST controller for managing Exam.
 */
@RestController
@RequestMapping("/api")
public class ExamResource {

    private final Logger log = LoggerFactory.getLogger(ExamResource.class);

    private static final String ENTITY_NAME = "examExam";

    private final ExamService examService;

    private final ExamQueryService examQueryService;

    public ExamResource(ExamService examService, ExamQueryService examQueryService) {
        this.examService = examService;
        this.examQueryService = examQueryService;
    }

    /**
     * POST  /exams : Create a new exam.
     *
     * @param exam the exam to create
     * @return the ResponseEntity with status 201 (Created) and with body the new exam, or with status 400 (Bad Request) if the exam has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/exams")
    @Timed
    public ResponseEntity<Exam> createExam(@RequestBody Exam exam) throws URISyntaxException {
        log.debug("REST request to save Exam : {}", exam);
        if (exam.getId() != null) {
            throw new BadRequestAlertException("A new exam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Exam result = examService.save(exam);
        return ResponseEntity.created(new URI("/api/exams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /exams : Updates an existing exam.
     *
     * @param exam the exam to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated exam,
     * or with status 400 (Bad Request) if the exam is not valid,
     * or with status 500 (Internal Server Error) if the exam couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/exams")
    @Timed
    public ResponseEntity<Exam> updateExam(@RequestBody Exam exam) throws URISyntaxException {
        log.debug("REST request to update Exam : {}", exam);
        if (exam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Exam result = examService.save(exam);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, exam.getId().toString()))
            .body(result);
    }

    /**
     * GET  /exams : get all the exams.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of exams in body
     */
    @GetMapping("/exams")
    @Timed
    public ResponseEntity<List<Exam>> getAllExams(ExamCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Exams by criteria: {}", criteria);
        Page<Exam> page = examQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/exams");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /exams/:id : get the "id" exam.
     *
     * @param id the id of the exam to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the exam, or with status 404 (Not Found)
     */
    @GetMapping("/exams/{id}")
    @Timed
    public ResponseEntity<Exam> getExam(@PathVariable Long id) {
        log.debug("REST request to get Exam : {}", id);
        Optional<Exam> exam = examService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exam);
    }

    /**
     * DELETE  /exams/:id : delete the "id" exam.
     *
     * @param id the id of the exam to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/exams/{id}")
    @Timed
    public ResponseEntity<Void> deleteExam(@PathVariable Long id) {
        log.debug("REST request to delete Exam : {}", id);
        examService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
