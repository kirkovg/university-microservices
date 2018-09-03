package mk.ukim.finki.broadcast.service.impl;

import mk.ukim.finki.broadcast.service.EntryService;
import mk.ukim.finki.broadcast.domain.Entry;
import mk.ukim.finki.broadcast.repository.EntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Entry.
 */
@Service
@Transactional
public class EntryServiceImpl implements EntryService {

    private final Logger log = LoggerFactory.getLogger(EntryServiceImpl.class);

    private final EntryRepository entryRepository;

    public EntryServiceImpl(EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    /**
     * Save a entry.
     *
     * @param entry the entity to save
     * @return the persisted entity
     */
    @Override
    public Entry save(Entry entry) {
        log.debug("Request to save Entry : {}", entry);        return entryRepository.save(entry);
    }

    /**
     * Get all the entries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Entry> findAll(Pageable pageable) {
        log.debug("Request to get all Entries");
        return entryRepository.findAll(pageable);
    }


    /**
     * Get one entry by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Entry> findOne(Long id) {
        log.debug("Request to get Entry : {}", id);
        return entryRepository.findById(id);
    }

    /**
     * Delete the entry by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Entry : {}", id);
        entryRepository.deleteById(id);
    }
}
