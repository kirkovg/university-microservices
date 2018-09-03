package mk.ukim.finki.emergency.web.rest;

import mk.ukim.finki.emergency.EmergencyApp;

import mk.ukim.finki.emergency.domain.Entry;
import mk.ukim.finki.emergency.repository.EntryRepository;
import mk.ukim.finki.emergency.service.EntryService;
import mk.ukim.finki.emergency.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.emergency.service.dto.EntryCriteria;
import mk.ukim.finki.emergency.service.EntryQueryService;

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
import java.util.List;


import static mk.ukim.finki.emergency.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the EntryResource REST controller.
 *
 * @see EntryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmergencyApp.class)
public class EntryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CONTACT = "AAAAAAAAAA";
    private static final String UPDATED_CONTACT = "BBBBBBBBBB";

    @Autowired
    private EntryRepository entryRepository;
    
    @Autowired
    private EntryService entryService;

    @Autowired
    private EntryQueryService entryQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEntryMockMvc;

    private Entry entry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EntryResource entryResource = new EntryResource(entryService, entryQueryService);
        this.restEntryMockMvc = MockMvcBuilders.standaloneSetup(entryResource)
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
    public static Entry createEntity(EntityManager em) {
        Entry entry = new Entry()
            .name(DEFAULT_NAME)
            .contact(DEFAULT_CONTACT);
        return entry;
    }

    @Before
    public void initTest() {
        entry = createEntity(em);
    }

    @Test
    @Transactional
    public void createEntry() throws Exception {
        int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry
        restEntryMockMvc.perform(post("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isCreated());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeCreate + 1);
        Entry testEntry = entryList.get(entryList.size() - 1);
        assertThat(testEntry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEntry.getContact()).isEqualTo(DEFAULT_CONTACT);
    }

    @Test
    @Transactional
    public void createEntryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = entryRepository.findAll().size();

        // Create the Entry with an existing ID
        entry.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEntryMockMvc.perform(post("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEntries() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entryList
        restEntryMockMvc.perform(get("/api/entries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())));
    }
    
    @Test
    @Transactional
    public void getEntry() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get the entry
        restEntryMockMvc.perform(get("/api/entries/{id}", entry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(entry.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.contact").value(DEFAULT_CONTACT.toString()));
    }

    @Test
    @Transactional
    public void getAllEntriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entryList where name equals to DEFAULT_NAME
        defaultEntryShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the entryList where name equals to UPDATED_NAME
        defaultEntryShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEntriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entryList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEntryShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the entryList where name equals to UPDATED_NAME
        defaultEntryShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEntriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entryList where name is not null
        defaultEntryShouldBeFound("name.specified=true");

        // Get all the entryList where name is null
        defaultEntryShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllEntriesByContactIsEqualToSomething() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entryList where contact equals to DEFAULT_CONTACT
        defaultEntryShouldBeFound("contact.equals=" + DEFAULT_CONTACT);

        // Get all the entryList where contact equals to UPDATED_CONTACT
        defaultEntryShouldNotBeFound("contact.equals=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllEntriesByContactIsInShouldWork() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entryList where contact in DEFAULT_CONTACT or UPDATED_CONTACT
        defaultEntryShouldBeFound("contact.in=" + DEFAULT_CONTACT + "," + UPDATED_CONTACT);

        // Get all the entryList where contact equals to UPDATED_CONTACT
        defaultEntryShouldNotBeFound("contact.in=" + UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void getAllEntriesByContactIsNullOrNotNull() throws Exception {
        // Initialize the database
        entryRepository.saveAndFlush(entry);

        // Get all the entryList where contact is not null
        defaultEntryShouldBeFound("contact.specified=true");

        // Get all the entryList where contact is null
        defaultEntryShouldNotBeFound("contact.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultEntryShouldBeFound(String filter) throws Exception {
        restEntryMockMvc.perform(get("/api/entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(entry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].contact").value(hasItem(DEFAULT_CONTACT.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultEntryShouldNotBeFound(String filter) throws Exception {
        restEntryMockMvc.perform(get("/api/entries?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingEntry() throws Exception {
        // Get the entry
        restEntryMockMvc.perform(get("/api/entries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEntry() throws Exception {
        // Initialize the database
        entryService.save(entry);

        int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Update the entry
        Entry updatedEntry = entryRepository.findById(entry.getId()).get();
        // Disconnect from session so that the updates on updatedEntry are not directly saved in db
        em.detach(updatedEntry);
        updatedEntry
            .name(UPDATED_NAME)
            .contact(UPDATED_CONTACT);

        restEntryMockMvc.perform(put("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEntry)))
            .andExpect(status().isOk());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeUpdate);
        Entry testEntry = entryList.get(entryList.size() - 1);
        assertThat(testEntry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEntry.getContact()).isEqualTo(UPDATED_CONTACT);
    }

    @Test
    @Transactional
    public void updateNonExistingEntry() throws Exception {
        int databaseSizeBeforeUpdate = entryRepository.findAll().size();

        // Create the Entry

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEntryMockMvc.perform(put("/api/entries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(entry)))
            .andExpect(status().isBadRequest());

        // Validate the Entry in the database
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEntry() throws Exception {
        // Initialize the database
        entryService.save(entry);

        int databaseSizeBeforeDelete = entryRepository.findAll().size();

        // Get the entry
        restEntryMockMvc.perform(delete("/api/entries/{id}", entry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Entry> entryList = entryRepository.findAll();
        assertThat(entryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Entry.class);
        Entry entry1 = new Entry();
        entry1.setId(1L);
        Entry entry2 = new Entry();
        entry2.setId(entry1.getId());
        assertThat(entry1).isEqualTo(entry2);
        entry2.setId(2L);
        assertThat(entry1).isNotEqualTo(entry2);
        entry1.setId(null);
        assertThat(entry1).isNotEqualTo(entry2);
    }
}
