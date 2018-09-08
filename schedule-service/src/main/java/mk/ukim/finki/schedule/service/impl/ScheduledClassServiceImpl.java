package mk.ukim.finki.schedule.service.impl;

import mk.ukim.finki.schedule.service.ScheduledClassService;
import mk.ukim.finki.schedule.domain.ScheduledClass;
import mk.ukim.finki.schedule.repository.ScheduledClassRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing ScheduledClass.
 */
@Service
@Transactional
public class ScheduledClassServiceImpl implements ScheduledClassService {

    private final Logger log = LoggerFactory.getLogger(ScheduledClassServiceImpl.class);

    private final ScheduledClassRepository scheduledClassRepository;

    public ScheduledClassServiceImpl(ScheduledClassRepository scheduledClassRepository) {
        this.scheduledClassRepository = scheduledClassRepository;
    }

    /**
     * Save a scheduledClass.
     *
     * @param scheduledClass the entity to save
     * @return the persisted entity
     */
    @Override
    public ScheduledClass save(ScheduledClass scheduledClass) {
        log.debug("Request to save ScheduledClass : {}", scheduledClass);        return scheduledClassRepository.save(scheduledClass);
    }

    /**
     * Get all the scheduledClasses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ScheduledClass> findAll(Pageable pageable) {
        log.debug("Request to get all ScheduledClasses");
        return scheduledClassRepository.findAll(pageable);
    }


    /**
     * Get one scheduledClass by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ScheduledClass> findOne(Long id) {
        log.debug("Request to get ScheduledClass : {}", id);
        return scheduledClassRepository.findById(id);
    }

    /**
     * Delete the scheduledClass by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ScheduledClass : {}", id);
        scheduledClassRepository.deleteById(id);
    }
}
