package mk.ukim.finki.exam.service.impl;

import mk.ukim.finki.exam.service.AppuserService;
import mk.ukim.finki.exam.domain.Appuser;
import mk.ukim.finki.exam.repository.AppuserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Appuser.
 */
@Service
@Transactional
public class AppuserServiceImpl implements AppuserService {

    private final Logger log = LoggerFactory.getLogger(AppuserServiceImpl.class);

    private final AppuserRepository appuserRepository;

    public AppuserServiceImpl(AppuserRepository appuserRepository) {
        this.appuserRepository = appuserRepository;
    }

    /**
     * Save a appuser.
     *
     * @param appuser the entity to save
     * @return the persisted entity
     */
    @Override
    public Appuser save(Appuser appuser) {
        log.debug("Request to save Appuser : {}", appuser);        return appuserRepository.save(appuser);
    }

    /**
     * Get all the appusers.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Appuser> findAll(Pageable pageable) {
        log.debug("Request to get all Appusers");
        return appuserRepository.findAll(pageable);
    }


    /**
     * Get one appuser by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Appuser> findOne(Long id) {
        log.debug("Request to get Appuser : {}", id);
        return appuserRepository.findById(id);
    }

    /**
     * Delete the appuser by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Appuser : {}", id);
        appuserRepository.deleteById(id);
    }
}
