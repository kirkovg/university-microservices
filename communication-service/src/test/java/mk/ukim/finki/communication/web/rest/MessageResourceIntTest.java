package mk.ukim.finki.communication.web.rest;

import mk.ukim.finki.communication.CommunicationApp;

import mk.ukim.finki.communication.domain.Message;
import mk.ukim.finki.communication.domain.Appuser;
import mk.ukim.finki.communication.domain.Appuser;
import mk.ukim.finki.communication.repository.MessageRepository;
import mk.ukim.finki.communication.service.MessageService;
import mk.ukim.finki.communication.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.communication.service.dto.MessageCriteria;
import mk.ukim.finki.communication.service.MessageQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static mk.ukim.finki.communication.web.rest.TestUtil.sameInstant;
import static mk.ukim.finki.communication.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MessageResource REST controller.
 *
 * @see MessageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommunicationApp.class)
public class MessageResourceIntTest {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_SENT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SENT_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageQueryService messageQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMessageMockMvc;

    private Message message;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MessageResource messageResource = new MessageResource(messageService, messageQueryService);
        this.restMessageMockMvc = MockMvcBuilders.standaloneSetup(messageResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Message createEntity(EntityManager em) {
        Message message = new Message()
            .content(DEFAULT_CONTENT)
            .sentAt(DEFAULT_SENT_AT);
        return message;
    }

    @Before
    public void initTest() {
        message = createEntity(em);
    }

    @Test
    @Transactional
    public void createMessage() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isCreated());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate + 1);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testMessage.getSentAt()).isEqualTo(DEFAULT_SENT_AT);
    }

    @Test
    @Transactional
    public void createMessageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = messageRepository.findAll().size();

        // Create the Message with an existing ID
        message.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMessageMockMvc.perform(post("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMessages() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(sameInstant(DEFAULT_SENT_AT))));
    }
    
    @Test
    @Transactional
    public void getMessage() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", message.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(message.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.sentAt").value(sameInstant(DEFAULT_SENT_AT)));
    }

    @Test
    @Transactional
    public void getAllMessagesByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where content equals to DEFAULT_CONTENT
        defaultMessageShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the messageList where content equals to UPDATED_CONTENT
        defaultMessageShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllMessagesByContentIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultMessageShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the messageList where content equals to UPDATED_CONTENT
        defaultMessageShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllMessagesByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where content is not null
        defaultMessageShouldBeFound("content.specified=true");

        // Get all the messageList where content is null
        defaultMessageShouldNotBeFound("content.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesBySentAtIsEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where sentAt equals to DEFAULT_SENT_AT
        defaultMessageShouldBeFound("sentAt.equals=" + DEFAULT_SENT_AT);

        // Get all the messageList where sentAt equals to UPDATED_SENT_AT
        defaultMessageShouldNotBeFound("sentAt.equals=" + UPDATED_SENT_AT);
    }

    @Test
    @Transactional
    public void getAllMessagesBySentAtIsInShouldWork() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where sentAt in DEFAULT_SENT_AT or UPDATED_SENT_AT
        defaultMessageShouldBeFound("sentAt.in=" + DEFAULT_SENT_AT + "," + UPDATED_SENT_AT);

        // Get all the messageList where sentAt equals to UPDATED_SENT_AT
        defaultMessageShouldNotBeFound("sentAt.in=" + UPDATED_SENT_AT);
    }

    @Test
    @Transactional
    public void getAllMessagesBySentAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where sentAt is not null
        defaultMessageShouldBeFound("sentAt.specified=true");

        // Get all the messageList where sentAt is null
        defaultMessageShouldNotBeFound("sentAt.specified=false");
    }

    @Test
    @Transactional
    public void getAllMessagesBySentAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where sentAt greater than or equals to DEFAULT_SENT_AT
        defaultMessageShouldBeFound("sentAt.greaterOrEqualThan=" + DEFAULT_SENT_AT);

        // Get all the messageList where sentAt greater than or equals to UPDATED_SENT_AT
        defaultMessageShouldNotBeFound("sentAt.greaterOrEqualThan=" + UPDATED_SENT_AT);
    }

    @Test
    @Transactional
    public void getAllMessagesBySentAtIsLessThanSomething() throws Exception {
        // Initialize the database
        messageRepository.saveAndFlush(message);

        // Get all the messageList where sentAt less than or equals to DEFAULT_SENT_AT
        defaultMessageShouldNotBeFound("sentAt.lessThan=" + DEFAULT_SENT_AT);

        // Get all the messageList where sentAt less than or equals to UPDATED_SENT_AT
        defaultMessageShouldBeFound("sentAt.lessThan=" + UPDATED_SENT_AT);
    }


    @Test
    @Transactional
    public void getAllMessagesByFromIsEqualToSomething() throws Exception {
        // Initialize the database
        Appuser from = AppuserResourceIntTest.createEntity(em);
        em.persist(from);
        em.flush();
        message.setFrom(from);
        messageRepository.saveAndFlush(message);
        Long fromId = from.getId();

        // Get all the messageList where from equals to fromId
        defaultMessageShouldBeFound("fromId.equals=" + fromId);

        // Get all the messageList where from equals to fromId + 1
        defaultMessageShouldNotBeFound("fromId.equals=" + (fromId + 1));
    }


    @Test
    @Transactional
    public void getAllMessagesByToIsEqualToSomething() throws Exception {
        // Initialize the database
        Appuser to = AppuserResourceIntTest.createEntity(em);
        em.persist(to);
        em.flush();
        message.setTo(to);
        messageRepository.saveAndFlush(message);
        Long toId = to.getId();

        // Get all the messageList where to equals to toId
        defaultMessageShouldBeFound("toId.equals=" + toId);

        // Get all the messageList where to equals to toId + 1
        defaultMessageShouldNotBeFound("toId.equals=" + (toId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultMessageShouldBeFound(String filter) throws Exception {
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(message.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].sentAt").value(hasItem(sameInstant(DEFAULT_SENT_AT))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultMessageShouldNotBeFound(String filter) throws Exception {
        restMessageMockMvc.perform(get("/api/messages?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingMessage() throws Exception {
        // Get the message
        restMessageMockMvc.perform(get("/api/messages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMessage() throws Exception {
        // Initialize the database
        messageService.save(message);

        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Update the message
        Message updatedMessage = messageRepository.findById(message.getId()).get();
        // Disconnect from session so that the updates on updatedMessage are not directly saved in db
        em.detach(updatedMessage);
        updatedMessage
            .content(UPDATED_CONTENT)
            .sentAt(UPDATED_SENT_AT);

        restMessageMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMessage)))
            .andExpect(status().isOk());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
        Message testMessage = messageList.get(messageList.size() - 1);
        assertThat(testMessage.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testMessage.getSentAt()).isEqualTo(UPDATED_SENT_AT);
    }

    @Test
    @Transactional
    public void updateNonExistingMessage() throws Exception {
        int databaseSizeBeforeUpdate = messageRepository.findAll().size();

        // Create the Message

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMessageMockMvc.perform(put("/api/messages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(message)))
            .andExpect(status().isBadRequest());

        // Validate the Message in the database
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMessage() throws Exception {
        // Initialize the database
        messageService.save(message);

        int databaseSizeBeforeDelete = messageRepository.findAll().size();

        // Get the message
        restMessageMockMvc.perform(delete("/api/messages/{id}", message.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Message> messageList = messageRepository.findAll();
        assertThat(messageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = new Message();
        message1.setId(1L);
        Message message2 = new Message();
        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);
        message2.setId(2L);
        assertThat(message1).isNotEqualTo(message2);
        message1.setId(null);
        assertThat(message1).isNotEqualTo(message2);
    }
}
