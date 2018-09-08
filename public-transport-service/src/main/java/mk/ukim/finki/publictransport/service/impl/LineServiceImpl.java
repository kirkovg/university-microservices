package mk.ukim.finki.publictransport.service.impl;

import mk.ukim.finki.publictransport.service.LineService;
import mk.ukim.finki.publictransport.domain.Line;
import mk.ukim.finki.publictransport.repository.LineRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing Line.
 */
@Service
@Transactional
public class LineServiceImpl implements LineService {

    private final Logger log = LoggerFactory.getLogger(LineServiceImpl.class);

    private final LineRepository lineRepository;

    public LineServiceImpl(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    /**
     * Save a line.
     *
     * @param line the entity to save
     * @return the persisted entity
     */
    @Override
    public Line save(Line line) {
        log.debug("Request to save Line : {}", line);        return lineRepository.save(line);
    }

    /**
     * Get all the lines.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Line> findAll(Pageable pageable) {
        log.debug("Request to get all Lines");
        return lineRepository.findAll(pageable);
    }


    /**
     * Get one line by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Line> findOne(Long id) {
        log.debug("Request to get Line : {}", id);
        return lineRepository.findById(id);
    }

    /**
     * Delete the line by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Line : {}", id);
        lineRepository.deleteById(id);
    }
}
