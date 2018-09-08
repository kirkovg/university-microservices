package mk.ukim.finki.gradespreview.service.impl;

import mk.ukim.finki.gradespreview.service.CompletedCourseService;
import mk.ukim.finki.gradespreview.domain.CompletedCourse;
import mk.ukim.finki.gradespreview.repository.CompletedCourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing CompletedCourse.
 */
@Service
@Transactional
public class CompletedCourseServiceImpl implements CompletedCourseService {

    private final Logger log = LoggerFactory.getLogger(CompletedCourseServiceImpl.class);

    private final CompletedCourseRepository completedCourseRepository;

    public CompletedCourseServiceImpl(CompletedCourseRepository completedCourseRepository) {
        this.completedCourseRepository = completedCourseRepository;
    }

    /**
     * Save a completedCourse.
     *
     * @param completedCourse the entity to save
     * @return the persisted entity
     */
    @Override
    public CompletedCourse save(CompletedCourse completedCourse) {
        log.debug("Request to save CompletedCourse : {}", completedCourse);        return completedCourseRepository.save(completedCourse);
    }

    /**
     * Get all the completedCourses.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CompletedCourse> findAll(Pageable pageable) {
        log.debug("Request to get all CompletedCourses");
        return completedCourseRepository.findAll(pageable);
    }


    /**
     * Get one completedCourse by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CompletedCourse> findOne(Long id) {
        log.debug("Request to get CompletedCourse : {}", id);
        return completedCourseRepository.findById(id);
    }

    /**
     * Delete the completedCourse by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompletedCourse : {}", id);
        completedCourseRepository.deleteById(id);
    }
}
