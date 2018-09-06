package mk.ukim.finki.multimedia.service.impl;

import mk.ukim.finki.multimedia.service.FileService;
import mk.ukim.finki.multimedia.domain.File;
import mk.ukim.finki.multimedia.repository.FileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;
/**
 * Service Implementation for managing File.
 */
@Service
@Transactional
public class FileServiceImpl implements FileService {

    private final Logger log = LoggerFactory.getLogger(FileServiceImpl.class);

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    /**
     * Save a file.
     *
     * @param file the entity to save
     * @return the persisted entity
     */
    @Override
    public File save(File file) {
        log.debug("Request to save File : {}", file);        return fileRepository.save(file);
    }

    /**
     * Get all the files.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<File> findAll(Pageable pageable) {
        log.debug("Request to get all Files");
        return fileRepository.findAll(pageable);
    }


    /**
     * Get one file by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<File> findOne(Long id) {
        log.debug("Request to get File : {}", id);
        return fileRepository.findById(id);
    }

    /**
     * Delete the file by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete File : {}", id);
        fileRepository.deleteById(id);
    }
}
