package mk.ukim.finki.admission.web.rest;

import mk.ukim.finki.admission.AdmissionApp;

import mk.ukim.finki.admission.domain.Program;
import mk.ukim.finki.admission.domain.Document;
import mk.ukim.finki.admission.repository.ProgramRepository;
import mk.ukim.finki.admission.service.ProgramService;
import mk.ukim.finki.admission.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.admission.service.dto.ProgramCriteria;
import mk.ukim.finki.admission.service.ProgramQueryService;

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


import static mk.ukim.finki.admission.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ProgramResource REST controller.
 *
 * @see ProgramResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AdmissionApp.class)
public class ProgramResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_LENGTH_IN_YEARS = 1;
    private static final Integer UPDATED_LENGTH_IN_YEARS = 2;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private ProgramRepository programRepository;
    
    @Autowired
    private ProgramService programService;

    @Autowired
    private ProgramQueryService programQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restProgramMockMvc;

    private Program program;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProgramResource programResource = new ProgramResource(programService, programQueryService);
        this.restProgramMockMvc = MockMvcBuilders.standaloneSetup(programResource)
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
    public static Program createEntity(EntityManager em) {
        Program program = new Program()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .lengthInYears(DEFAULT_LENGTH_IN_YEARS)
            .title(DEFAULT_TITLE);
        return program;
    }

    @Before
    public void initTest() {
        program = createEntity(em);
    }

    @Test
    @Transactional
    public void createProgram() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isCreated());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate + 1);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProgram.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProgram.getLengthInYears()).isEqualTo(DEFAULT_LENGTH_IN_YEARS);
        assertThat(testProgram.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createProgramWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = programRepository.findAll().size();

        // Create the Program with an existing ID
        program.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc.perform(post("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPrograms() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lengthInYears").value(hasItem(DEFAULT_LENGTH_IN_YEARS)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    
    @Test
    @Transactional
    public void getProgram() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.lengthInYears").value(DEFAULT_LENGTH_IN_YEARS))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getAllProgramsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name equals to DEFAULT_NAME
        defaultProgramShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the programList where name equals to UPDATED_NAME
        defaultProgramShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProgramsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProgramShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the programList where name equals to UPDATED_NAME
        defaultProgramShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProgramsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where name is not null
        defaultProgramShouldBeFound("name.specified=true");

        // Get all the programList where name is null
        defaultProgramShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllProgramsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where description equals to DEFAULT_DESCRIPTION
        defaultProgramShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the programList where description equals to UPDATED_DESCRIPTION
        defaultProgramShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProgramsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProgramShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the programList where description equals to UPDATED_DESCRIPTION
        defaultProgramShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProgramsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where description is not null
        defaultProgramShouldBeFound("description.specified=true");

        // Get all the programList where description is null
        defaultProgramShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllProgramsByLengthInYearsIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where lengthInYears equals to DEFAULT_LENGTH_IN_YEARS
        defaultProgramShouldBeFound("lengthInYears.equals=" + DEFAULT_LENGTH_IN_YEARS);

        // Get all the programList where lengthInYears equals to UPDATED_LENGTH_IN_YEARS
        defaultProgramShouldNotBeFound("lengthInYears.equals=" + UPDATED_LENGTH_IN_YEARS);
    }

    @Test
    @Transactional
    public void getAllProgramsByLengthInYearsIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where lengthInYears in DEFAULT_LENGTH_IN_YEARS or UPDATED_LENGTH_IN_YEARS
        defaultProgramShouldBeFound("lengthInYears.in=" + DEFAULT_LENGTH_IN_YEARS + "," + UPDATED_LENGTH_IN_YEARS);

        // Get all the programList where lengthInYears equals to UPDATED_LENGTH_IN_YEARS
        defaultProgramShouldNotBeFound("lengthInYears.in=" + UPDATED_LENGTH_IN_YEARS);
    }

    @Test
    @Transactional
    public void getAllProgramsByLengthInYearsIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where lengthInYears is not null
        defaultProgramShouldBeFound("lengthInYears.specified=true");

        // Get all the programList where lengthInYears is null
        defaultProgramShouldNotBeFound("lengthInYears.specified=false");
    }

    @Test
    @Transactional
    public void getAllProgramsByLengthInYearsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where lengthInYears greater than or equals to DEFAULT_LENGTH_IN_YEARS
        defaultProgramShouldBeFound("lengthInYears.greaterOrEqualThan=" + DEFAULT_LENGTH_IN_YEARS);

        // Get all the programList where lengthInYears greater than or equals to UPDATED_LENGTH_IN_YEARS
        defaultProgramShouldNotBeFound("lengthInYears.greaterOrEqualThan=" + UPDATED_LENGTH_IN_YEARS);
    }

    @Test
    @Transactional
    public void getAllProgramsByLengthInYearsIsLessThanSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where lengthInYears less than or equals to DEFAULT_LENGTH_IN_YEARS
        defaultProgramShouldNotBeFound("lengthInYears.lessThan=" + DEFAULT_LENGTH_IN_YEARS);

        // Get all the programList where lengthInYears less than or equals to UPDATED_LENGTH_IN_YEARS
        defaultProgramShouldBeFound("lengthInYears.lessThan=" + UPDATED_LENGTH_IN_YEARS);
    }


    @Test
    @Transactional
    public void getAllProgramsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where title equals to DEFAULT_TITLE
        defaultProgramShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the programList where title equals to UPDATED_TITLE
        defaultProgramShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProgramsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultProgramShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the programList where title equals to UPDATED_TITLE
        defaultProgramShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllProgramsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        programRepository.saveAndFlush(program);

        // Get all the programList where title is not null
        defaultProgramShouldBeFound("title.specified=true");

        // Get all the programList where title is null
        defaultProgramShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllProgramsByDocumentIsEqualToSomething() throws Exception {
        // Initialize the database
        Document document = DocumentResourceIntTest.createEntity(em);
        em.persist(document);
        em.flush();
        program.addDocument(document);
        programRepository.saveAndFlush(program);
        Long documentId = document.getId();

        // Get all the programList where document equals to documentId
        defaultProgramShouldBeFound("documentId.equals=" + documentId);

        // Get all the programList where document equals to documentId + 1
        defaultProgramShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultProgramShouldBeFound(String filter) throws Exception {
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lengthInYears").value(hasItem(DEFAULT_LENGTH_IN_YEARS)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultProgramShouldNotBeFound(String filter) throws Exception {
        restProgramMockMvc.perform(get("/api/programs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get("/api/programs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProgram() throws Exception {
        // Initialize the database
        programService.save(program);

        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).get();
        // Disconnect from session so that the updates on updatedProgram are not directly saved in db
        em.detach(updatedProgram);
        updatedProgram
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .lengthInYears(UPDATED_LENGTH_IN_YEARS)
            .title(UPDATED_TITLE);

        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProgram)))
            .andExpect(status().isOk());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
        Program testProgram = programList.get(programList.size() - 1);
        assertThat(testProgram.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProgram.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProgram.getLengthInYears()).isEqualTo(UPDATED_LENGTH_IN_YEARS);
        assertThat(testProgram.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingProgram() throws Exception {
        int databaseSizeBeforeUpdate = programRepository.findAll().size();

        // Create the Program

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc.perform(put("/api/programs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(program)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProgram() throws Exception {
        // Initialize the database
        programService.save(program);

        int databaseSizeBeforeDelete = programRepository.findAll().size();

        // Get the program
        restProgramMockMvc.perform(delete("/api/programs/{id}", program.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Program> programList = programRepository.findAll();
        assertThat(programList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Program.class);
        Program program1 = new Program();
        program1.setId(1L);
        Program program2 = new Program();
        program2.setId(program1.getId());
        assertThat(program1).isEqualTo(program2);
        program2.setId(2L);
        assertThat(program1).isNotEqualTo(program2);
        program1.setId(null);
        assertThat(program1).isNotEqualTo(program2);
    }
}
