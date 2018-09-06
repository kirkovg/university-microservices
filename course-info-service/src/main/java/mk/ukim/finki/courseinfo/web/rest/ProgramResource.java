package mk.ukim.finki.courseinfo.web.rest;

import com.codahale.metrics.annotation.Timed;
import mk.ukim.finki.courseinfo.domain.Program;
import mk.ukim.finki.courseinfo.service.ProgramService;
import mk.ukim.finki.courseinfo.web.rest.errors.BadRequestAlertException;
import mk.ukim.finki.courseinfo.web.rest.util.HeaderUtil;
import mk.ukim.finki.courseinfo.web.rest.util.PaginationUtil;
import mk.ukim.finki.courseinfo.service.dto.ProgramCriteria;
import mk.ukim.finki.courseinfo.service.ProgramQueryService;
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
 * REST controller for managing Program.
 */
@RestController
@RequestMapping("/api")
public class ProgramResource {

    private final Logger log = LoggerFactory.getLogger(ProgramResource.class);

    private static final String ENTITY_NAME = "courseinfoProgram";

    private final ProgramService programService;

    private final ProgramQueryService programQueryService;

    public ProgramResource(ProgramService programService, ProgramQueryService programQueryService) {
        this.programService = programService;
        this.programQueryService = programQueryService;
    }

    /**
     * POST  /programs : Create a new program.
     *
     * @param program the program to create
     * @return the ResponseEntity with status 201 (Created) and with body the new program, or with status 400 (Bad Request) if the program has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/programs")
    @Timed
    public ResponseEntity<Program> createProgram(@RequestBody Program program) throws URISyntaxException {
        log.debug("REST request to save Program : {}", program);
        if (program.getId() != null) {
            throw new BadRequestAlertException("A new program cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Program result = programService.save(program);
        return ResponseEntity.created(new URI("/api/programs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /programs : Updates an existing program.
     *
     * @param program the program to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated program,
     * or with status 400 (Bad Request) if the program is not valid,
     * or with status 500 (Internal Server Error) if the program couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/programs")
    @Timed
    public ResponseEntity<Program> updateProgram(@RequestBody Program program) throws URISyntaxException {
        log.debug("REST request to update Program : {}", program);
        if (program.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Program result = programService.save(program);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, program.getId().toString()))
            .body(result);
    }

    /**
     * GET  /programs : get all the programs.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of programs in body
     */
    @GetMapping("/programs")
    @Timed
    public ResponseEntity<List<Program>> getAllPrograms(ProgramCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Programs by criteria: {}", criteria);
        Page<Program> page = programQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/programs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /programs/:id : get the "id" program.
     *
     * @param id the id of the program to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the program, or with status 404 (Not Found)
     */
    @GetMapping("/programs/{id}")
    @Timed
    public ResponseEntity<Program> getProgram(@PathVariable Long id) {
        log.debug("REST request to get Program : {}", id);
        Optional<Program> program = programService.findOne(id);
        return ResponseUtil.wrapOrNotFound(program);
    }

    /**
     * DELETE  /programs/:id : delete the "id" program.
     *
     * @param id the id of the program to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/programs/{id}")
    @Timed
    public ResponseEntity<Void> deleteProgram(@PathVariable Long id) {
        log.debug("REST request to delete Program : {}", id);
        programService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
