package mk.ukim.finki.officehours.web.rest;

import com.codahale.metrics.annotation.Timed;
import mk.ukim.finki.officehours.domain.Scheduledclass;
import mk.ukim.finki.officehours.service.ScheduledclassService;
import mk.ukim.finki.officehours.web.rest.errors.BadRequestAlertException;
import mk.ukim.finki.officehours.web.rest.util.HeaderUtil;
import mk.ukim.finki.officehours.web.rest.util.PaginationUtil;
import mk.ukim.finki.officehours.service.dto.ScheduledclassCriteria;
import mk.ukim.finki.officehours.service.ScheduledclassQueryService;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Scheduledclass.
 */
@RestController
@RequestMapping("/api")
public class ScheduledclassResource {

    private final Logger log = LoggerFactory.getLogger(ScheduledclassResource.class);

    private static final String ENTITY_NAME = "officehoursScheduledclass";

    private final ScheduledclassService scheduledclassService;

    private final ScheduledclassQueryService scheduledclassQueryService;

    public ScheduledclassResource(ScheduledclassService scheduledclassService, ScheduledclassQueryService scheduledclassQueryService) {
        this.scheduledclassService = scheduledclassService;
        this.scheduledclassQueryService = scheduledclassQueryService;
    }

    /**
     * POST  /scheduledclasses : Create a new scheduledclass.
     *
     * @param scheduledclass the scheduledclass to create
     * @return the ResponseEntity with status 201 (Created) and with body the new scheduledclass, or with status 400 (Bad Request) if the scheduledclass has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/scheduledclasses")
    @Timed
    public ResponseEntity<Scheduledclass> createScheduledclass(@Valid @RequestBody Scheduledclass scheduledclass) throws URISyntaxException {
        log.debug("REST request to save Scheduledclass : {}", scheduledclass);
        if (scheduledclass.getId() != null) {
            throw new BadRequestAlertException("A new scheduledclass cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Scheduledclass result = scheduledclassService.save(scheduledclass);
        return ResponseEntity.created(new URI("/api/scheduledclasses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /scheduledclasses : Updates an existing scheduledclass.
     *
     * @param scheduledclass the scheduledclass to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated scheduledclass,
     * or with status 400 (Bad Request) if the scheduledclass is not valid,
     * or with status 500 (Internal Server Error) if the scheduledclass couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/scheduledclasses")
    @Timed
    public ResponseEntity<Scheduledclass> updateScheduledclass(@Valid @RequestBody Scheduledclass scheduledclass) throws URISyntaxException {
        log.debug("REST request to update Scheduledclass : {}", scheduledclass);
        if (scheduledclass.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Scheduledclass result = scheduledclassService.save(scheduledclass);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, scheduledclass.getId().toString()))
            .body(result);
    }

    /**
     * GET  /scheduledclasses : get all the scheduledclasses.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of scheduledclasses in body
     */
    @GetMapping("/scheduledclasses")
    @Timed
    public ResponseEntity<List<Scheduledclass>> getAllScheduledclasses(ScheduledclassCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Scheduledclasses by criteria: {}", criteria);
        Page<Scheduledclass> page = scheduledclassQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/scheduledclasses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /scheduledclasses/:id : get the "id" scheduledclass.
     *
     * @param id the id of the scheduledclass to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the scheduledclass, or with status 404 (Not Found)
     */
    @GetMapping("/scheduledclasses/{id}")
    @Timed
    public ResponseEntity<Scheduledclass> getScheduledclass(@PathVariable Long id) {
        log.debug("REST request to get Scheduledclass : {}", id);
        Optional<Scheduledclass> scheduledclass = scheduledclassService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduledclass);
    }

    /**
     * DELETE  /scheduledclasses/:id : delete the "id" scheduledclass.
     *
     * @param id the id of the scheduledclass to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/scheduledclasses/{id}")
    @Timed
    public ResponseEntity<Void> deleteScheduledclass(@PathVariable Long id) {
        log.debug("REST request to delete Scheduledclass : {}", id);
        scheduledclassService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
