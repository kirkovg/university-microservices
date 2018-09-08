package mk.ukim.finki.schedule.web.rest;

import com.codahale.metrics.annotation.Timed;
import mk.ukim.finki.schedule.domain.ScheduledClass;
import mk.ukim.finki.schedule.service.ScheduledClassService;
import mk.ukim.finki.schedule.web.rest.errors.BadRequestAlertException;
import mk.ukim.finki.schedule.web.rest.util.HeaderUtil;
import mk.ukim.finki.schedule.web.rest.util.PaginationUtil;
import mk.ukim.finki.schedule.service.dto.ScheduledClassCriteria;
import mk.ukim.finki.schedule.service.ScheduledClassQueryService;
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
 * REST controller for managing ScheduledClass.
 */
@RestController
@RequestMapping("/api")
public class ScheduledClassResource {

    private final Logger log = LoggerFactory.getLogger(ScheduledClassResource.class);

    private static final String ENTITY_NAME = "scheduleScheduledClass";

    private final ScheduledClassService scheduledClassService;

    private final ScheduledClassQueryService scheduledClassQueryService;

    public ScheduledClassResource(ScheduledClassService scheduledClassService, ScheduledClassQueryService scheduledClassQueryService) {
        this.scheduledClassService = scheduledClassService;
        this.scheduledClassQueryService = scheduledClassQueryService;
    }

    /**
     * POST  /scheduled-classes : Create a new scheduledClass.
     *
     * @param scheduledClass the scheduledClass to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduledClass, or with status 400 (Bad Request) if the scheduledClass has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scheduled-classes")
    @Timed
    public ResponseEntity<ScheduledClass> createScheduledClass(@RequestBody ScheduledClass scheduledClass) throws URISyntaxException {
        log.debug("REST request to save ScheduledClass : {}", scheduledClass);
        if (scheduledClass.getId() != null) {
            throw new BadRequestAlertException("A new scheduledClass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScheduledClass result = scheduledClassService.save(scheduledClass);
        return ResponseEntity.created(new URI("/api/scheduled-classes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scheduled-classes : Updates an existing scheduledClass.
     *
     * @param scheduledClass the scheduledClass to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduledClass,
     * or with status 400 (Bad Request) if the scheduledClass is not valid,
     * or with status 500 (Internal Server Error) if the scheduledClass couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scheduled-classes")
    @Timed
    public ResponseEntity<ScheduledClass> updateScheduledClass(@RequestBody ScheduledClass scheduledClass) throws URISyntaxException {
        log.debug("REST request to update ScheduledClass : {}", scheduledClass);
        if (scheduledClass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ScheduledClass result = scheduledClassService.save(scheduledClass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scheduledClass.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scheduled-classes : get all the scheduledClasses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of scheduledClasses in body
     */
    @GetMapping("/scheduled-classes")
    @Timed
    public ResponseEntity<List<ScheduledClass>> getAllScheduledClasses(ScheduledClassCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ScheduledClasses by criteria: {}", criteria);
        Page<ScheduledClass> page = scheduledClassQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scheduled-classes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scheduled-classes/:id : get the "id" scheduledClass.
     *
     * @param id the id of the scheduledClass to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduledClass, or with status 404 (Not Found)
     */
    @GetMapping("/scheduled-classes/{id}")
    @Timed
    public ResponseEntity<ScheduledClass> getScheduledClass(@PathVariable Long id) {
        log.debug("REST request to get ScheduledClass : {}", id);
        Optional<ScheduledClass> scheduledClass = scheduledClassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduledClass);
    }

    /**
     * DELETE  /scheduled-classes/:id : delete the "id" scheduledClass.
     *
     * @param id the id of the scheduledClass to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scheduled-classes/{id}")
    @Timed
    public ResponseEntity<Void> deleteScheduledClass(@PathVariable Long id) {
        log.debug("REST request to delete ScheduledClass : {}", id);
        scheduledClassService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
