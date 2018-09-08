package mk.ukim.finki.communication.web.rest;

import com.codahale.metrics.annotation.Timed;
import mk.ukim.finki.communication.domain.Appuser;
import mk.ukim.finki.communication.service.AppuserService;
import mk.ukim.finki.communication.web.rest.errors.BadRequestAlertException;
import mk.ukim.finki.communication.web.rest.util.HeaderUtil;
import mk.ukim.finki.communication.web.rest.util.PaginationUtil;
import mk.ukim.finki.communication.service.dto.AppuserCriteria;
import mk.ukim.finki.communication.service.AppuserQueryService;
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
 * REST controller for managing Appuser.
 */
@RestController
@RequestMapping("/api")
public class AppuserResource {

    private final Logger log = LoggerFactory.getLogger(AppuserResource.class);

    private static final String ENTITY_NAME = "communicationAppuser";

    private final AppuserService appuserService;

    private final AppuserQueryService appuserQueryService;

    public AppuserResource(AppuserService appuserService, AppuserQueryService appuserQueryService) {
        this.appuserService = appuserService;
        this.appuserQueryService = appuserQueryService;
    }

    /**
     * POST  /appusers : Create a new appuser.
     *
     * @param appuser the appuser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new appuser, or with status 400 (Bad Request) if the appuser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/appusers")
    @Timed
    public ResponseEntity<Appuser> createAppuser(@RequestBody Appuser appuser) throws URISyntaxException {
        log.debug("REST request to save Appuser : {}", appuser);
        if (appuser.getId() != null) {
            throw new BadRequestAlertException("A new appuser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Appuser result = appuserService.save(appuser);
        return ResponseEntity.created(new URI("/api/appusers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /appusers : Updates an existing appuser.
     *
     * @param appuser the appuser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated appuser,
     * or with status 400 (Bad Request) if the appuser is not valid,
     * or with status 500 (Internal Server Error) if the appuser couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/appusers")
    @Timed
    public ResponseEntity<Appuser> updateAppuser(@RequestBody Appuser appuser) throws URISyntaxException {
        log.debug("REST request to update Appuser : {}", appuser);
        if (appuser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Appuser result = appuserService.save(appuser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, appuser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /appusers : get all the appusers.
     *
     * @param pageable the pagination information
     * @param criteria the criterias which the requested entities should match
     * @return the ResponseEntity with status 200 (OK) and the list of appusers in body
     */
    @GetMapping("/appusers")
    @Timed
    public ResponseEntity<List<Appuser>> getAllAppusers(AppuserCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Appusers by criteria: {}", criteria);
        Page<Appuser> page = appuserQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/appusers");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /appusers/:id : get the "id" appuser.
     *
     * @param id the id of the appuser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the appuser, or with status 404 (Not Found)
     */
    @GetMapping("/appusers/{id}")
    @Timed
    public ResponseEntity<Appuser> getAppuser(@PathVariable Long id) {
        log.debug("REST request to get Appuser : {}", id);
        Optional<Appuser> appuser = appuserService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appuser);
    }

    /**
     * DELETE  /appusers/:id : delete the "id" appuser.
     *
     * @param id the id of the appuser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/appusers/{id}")
    @Timed
    public ResponseEntity<Void> deleteAppuser(@PathVariable Long id) {
        log.debug("REST request to delete Appuser : {}", id);
        appuserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
