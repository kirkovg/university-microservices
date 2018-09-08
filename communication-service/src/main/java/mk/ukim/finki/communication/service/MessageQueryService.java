package mk.ukim.finki.communication.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import mk.ukim.finki.communication.domain.Message;
import mk.ukim.finki.communication.domain.*; // for static metamodels
import mk.ukim.finki.communication.repository.MessageRepository;
import mk.ukim.finki.communication.service.dto.MessageCriteria;


/**
 * Service for executing complex queries for Message entities in the database.
 * The main input is a {@link MessageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Message} or a {@link Page} of {@link Message} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MessageQueryService extends QueryService<Message> {

    private final Logger log = LoggerFactory.getLogger(MessageQueryService.class);

    private final MessageRepository messageRepository;

    public MessageQueryService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    /**
     * Return a {@link List} of {@link Message} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Message> findByCriteria(MessageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Message> specification = createSpecification(criteria);
        return messageRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Message} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Message> findByCriteria(MessageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Message> specification = createSpecification(criteria);
        return messageRepository.findAll(specification, page);
    }

    /**
     * Function to convert MessageCriteria to a {@link Specification}
     */
    private Specification<Message> createSpecification(MessageCriteria criteria) {
        Specification<Message> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Message_.id));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), Message_.content));
            }
            if (criteria.getSentAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSentAt(), Message_.sentAt));
            }
            if (criteria.getFromId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getFromId(), Message_.from, Appuser_.id));
            }
            if (criteria.getToId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getToId(), Message_.to, Appuser_.id));
            }
        }
        return specification;
    }

}
