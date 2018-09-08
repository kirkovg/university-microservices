package mk.ukim.finki.exam.service.impl;

import mk.ukim.finki.exam.service.ExamService;
import mk.ukim.finki.exam.domain.Exam;
import mk.ukim.finki.exam.repository.ExamRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Exam.
 */
@Service
@Transactional
public class ExamServiceImpl implements ExamService {

    private final Logger log = LoggerFactory.getLogger(ExamServiceImpl.class);

    private final ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    /**
     * Save a exam.
     *
     * @param exam the entity to save
     * @return the persisted entity
     */
    @Override
    public Exam save(Exam exam) {
        log.debug("Request to save Exam : {}", exam);        return examRepository.save(exam);
    }

    /**
     * Get all the exams.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Exam> findAll(Pageable pageable) {
        log.debug("Request to get all Exams");
        return examRepository.findAll(pageable);
    }


    /**
     * Get one exam by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Exam> findOne(Long id) {
        log.debug("Request to get Exam : {}", id);
        return examRepository.findById(id);
    }

    /**
     * Delete the exam by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Exam : {}", id);
        examRepository.deleteById(id);
    }
}
