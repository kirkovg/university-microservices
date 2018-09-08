package mk.ukim.finki.exam.web.rest;

import mk.ukim.finki.exam.ExamApp;

import mk.ukim.finki.exam.domain.Exam;
import mk.ukim.finki.exam.repository.ExamRepository;
import mk.ukim.finki.exam.service.ExamService;
import mk.ukim.finki.exam.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.exam.service.dto.ExamCriteria;
import mk.ukim.finki.exam.service.ExamQueryService;

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


import static mk.ukim.finki.exam.web.rest.TestUtil.sameInstant;
import static mk.ukim.finki.exam.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExamResource REST controller.
 *
 * @see ExamResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExamApp.class)
public class ExamResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private ExamService examService;

    @Autowired
    private ExamQueryService examQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restExamMockMvc;

    private Exam exam;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExamResource examResource = new ExamResource(examService, examQueryService);
        this.restExamMockMvc = MockMvcBuilders.standaloneSetup(examResource)
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
    public static Exam createEntity(EntityManager em) {
        Exam exam = new Exam()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .time(DEFAULT_TIME);
        return exam;
    }

    @Before
    public void initTest() {
        exam = createEntity(em);
    }

    @Test
    @Transactional
    public void createExam() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();

        // Create the Exam
        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exam)))
            .andExpect(status().isCreated());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate + 1);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testExam.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testExam.getTime()).isEqualTo(DEFAULT_TIME);
    }

    @Test
    @Transactional
    public void createExamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();

        // Create the Exam with an existing ID
        exam.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exam)))
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllExams() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList
        restExamMockMvc.perform(get("/api/exams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exam.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }
    
    @Test
    @Transactional
    public void getExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get the exam
        restExamMockMvc.perform(get("/api/exams/{id}", exam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exam.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.time").value(sameInstant(DEFAULT_TIME)));
    }

    @Test
    @Transactional
    public void getAllExamsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where name equals to DEFAULT_NAME
        defaultExamShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the examList where name equals to UPDATED_NAME
        defaultExamShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExamsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where name in DEFAULT_NAME or UPDATED_NAME
        defaultExamShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the examList where name equals to UPDATED_NAME
        defaultExamShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllExamsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where name is not null
        defaultExamShouldBeFound("name.specified=true");

        // Get all the examList where name is null
        defaultExamShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllExamsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where location equals to DEFAULT_LOCATION
        defaultExamShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the examList where location equals to UPDATED_LOCATION
        defaultExamShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllExamsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultExamShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the examList where location equals to UPDATED_LOCATION
        defaultExamShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllExamsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where location is not null
        defaultExamShouldBeFound("location.specified=true");

        // Get all the examList where location is null
        defaultExamShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllExamsByTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where time equals to DEFAULT_TIME
        defaultExamShouldBeFound("time.equals=" + DEFAULT_TIME);

        // Get all the examList where time equals to UPDATED_TIME
        defaultExamShouldNotBeFound("time.equals=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllExamsByTimeIsInShouldWork() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where time in DEFAULT_TIME or UPDATED_TIME
        defaultExamShouldBeFound("time.in=" + DEFAULT_TIME + "," + UPDATED_TIME);

        // Get all the examList where time equals to UPDATED_TIME
        defaultExamShouldNotBeFound("time.in=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllExamsByTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where time is not null
        defaultExamShouldBeFound("time.specified=true");

        // Get all the examList where time is null
        defaultExamShouldNotBeFound("time.specified=false");
    }

    @Test
    @Transactional
    public void getAllExamsByTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where time greater than or equals to DEFAULT_TIME
        defaultExamShouldBeFound("time.greaterOrEqualThan=" + DEFAULT_TIME);

        // Get all the examList where time greater than or equals to UPDATED_TIME
        defaultExamShouldNotBeFound("time.greaterOrEqualThan=" + UPDATED_TIME);
    }

    @Test
    @Transactional
    public void getAllExamsByTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList where time less than or equals to DEFAULT_TIME
        defaultExamShouldNotBeFound("time.lessThan=" + DEFAULT_TIME);

        // Get all the examList where time less than or equals to UPDATED_TIME
        defaultExamShouldBeFound("time.lessThan=" + UPDATED_TIME);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultExamShouldBeFound(String filter) throws Exception {
        restExamMockMvc.perform(get("/api/exams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exam.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].time").value(hasItem(sameInstant(DEFAULT_TIME))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultExamShouldNotBeFound(String filter) throws Exception {
        restExamMockMvc.perform(get("/api/exams?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingExam() throws Exception {
        // Get the exam
        restExamMockMvc.perform(get("/api/exams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExam() throws Exception {
        // Initialize the database
        examService.save(exam);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam
        Exam updatedExam = examRepository.findById(exam.getId()).get();
        // Disconnect from session so that the updates on updatedExam are not directly saved in db
        em.detach(updatedExam);
        updatedExam
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .time(UPDATED_TIME);

        restExamMockMvc.perform(put("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExam)))
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testExam.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testExam.getTime()).isEqualTo(UPDATED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Create the Exam

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMockMvc.perform(put("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exam)))
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExam() throws Exception {
        // Initialize the database
        examService.save(exam);

        int databaseSizeBeforeDelete = examRepository.findAll().size();

        // Get the exam
        restExamMockMvc.perform(delete("/api/exams/{id}", exam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exam.class);
        Exam exam1 = new Exam();
        exam1.setId(1L);
        Exam exam2 = new Exam();
        exam2.setId(exam1.getId());
        assertThat(exam1).isEqualTo(exam2);
        exam2.setId(2L);
        assertThat(exam1).isNotEqualTo(exam2);
        exam1.setId(null);
        assertThat(exam1).isNotEqualTo(exam2);
    }
}
