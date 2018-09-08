package mk.ukim.finki.officehours.service.impl;

import mk.ukim.finki.officehours.service.ScheduledclassService;
import mk.ukim.finki.officehours.domain.Scheduledclass;
import mk.ukim.finki.officehours.repository.ScheduledclassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Scheduledclass.
 */
@Service
@Transactional
public class ScheduledclassServiceImpl implements ScheduledclassService {

    private final Logger log = LoggerFactory.getLogger(ScheduledclassServiceImpl.class);

    private final ScheduledclassRepository scheduledclassRepository;

    public ScheduledclassServiceImpl(ScheduledclassRepository scheduledclassRepository) {
        this.scheduledclassRepository = scheduledclassRepository;
    }

    /**
     * Save a scheduledclass.
     *
     * @param scheduledclass the entity to save
     * @return the persisted entity
     */
    @Override
    public Scheduledclass save(Scheduledclass scheduledclass) {
        log.debug("Request to save Scheduledclass : {}", scheduledclass);        return scheduledclassRepository.save(scheduledclass);
    }

    /**
     * Get all the scheduledclasses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Scheduledclass> findAll(Pageable pageable) {
        log.debug("Request to get all Scheduledclasses");
        return scheduledclassRepository.findAll(pageable);
    }


    /**
     * Get one scheduledclass by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Scheduledclass> findOne(Long id) {
        log.debug("Request to get Scheduledclass : {}", id);
        return scheduledclassRepository.findById(id);
    }

    /**
     * Delete the scheduledclass by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Scheduledclass : {}", id);
        scheduledclassRepository.deleteById(id);
    }
}
