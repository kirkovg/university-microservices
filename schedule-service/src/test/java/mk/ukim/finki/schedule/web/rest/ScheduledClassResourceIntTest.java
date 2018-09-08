package mk.ukim.finki.schedule.web.rest;

import mk.ukim.finki.schedule.ScheduleApp;

import mk.ukim.finki.schedule.domain.ScheduledClass;
import mk.ukim.finki.schedule.repository.ScheduledClassRepository;
import mk.ukim.finki.schedule.service.ScheduledClassService;
import mk.ukim.finki.schedule.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.schedule.service.dto.ScheduledClassCriteria;
import mk.ukim.finki.schedule.service.ScheduledClassQueryService;

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


import static mk.ukim.finki.schedule.web.rest.TestUtil.sameInstant;
import static mk.ukim.finki.schedule.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ScheduledClassResource REST controller.
 *
 * @see ScheduledClassResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ScheduleApp.class)
public class ScheduledClassResourceIntTest {

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LECTURER = "AAAAAAAAAA";
    private static final String UPDATED_LECTURER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ScheduledClassRepository scheduledClassRepository;
    
    @Autowired
    private ScheduledClassService scheduledClassService;

    @Autowired
    private ScheduledClassQueryService scheduledClassQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restScheduledClassMockMvc;

    private ScheduledClass scheduledClass;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScheduledClassResource scheduledClassResource = new ScheduledClassResource(scheduledClassService, scheduledClassQueryService);
        this.restScheduledClassMockMvc = MockMvcBuilders.standaloneSetup(scheduledClassResource)
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
    public static ScheduledClass createEntity(EntityManager em) {
        ScheduledClass scheduledClass = new ScheduledClass()
            .courseName(DEFAULT_COURSE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .lecturer(DEFAULT_LECTURER)
            .location(DEFAULT_LOCATION)
            .date(DEFAULT_DATE);
        return scheduledClass;
    }

    @Before
    public void initTest() {
        scheduledClass = createEntity(em);
    }

    @Test
    @Transactional
    public void createScheduledClass() throws Exception {
        int databaseSizeBeforeCreate = scheduledClassRepository.findAll().size();

        // Create the ScheduledClass
        restScheduledClassMockMvc.perform(post("/api/scheduled-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledClass)))
            .andExpect(status().isCreated());

        // Validate the ScheduledClass in the database
        List<ScheduledClass> scheduledClassList = scheduledClassRepository.findAll();
        assertThat(scheduledClassList).hasSize(databaseSizeBeforeCreate + 1);
        ScheduledClass testScheduledClass = scheduledClassList.get(scheduledClassList.size() - 1);
        assertThat(testScheduledClass.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testScheduledClass.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testScheduledClass.getLecturer()).isEqualTo(DEFAULT_LECTURER);
        assertThat(testScheduledClass.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testScheduledClass.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createScheduledClassWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scheduledClassRepository.findAll().size();

        // Create the ScheduledClass with an existing ID
        scheduledClass.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduledClassMockMvc.perform(post("/api/scheduled-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledClass)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduledClass in the database
        List<ScheduledClass> scheduledClassList = scheduledClassRepository.findAll();
        assertThat(scheduledClassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllScheduledClasses() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList
        restScheduledClassMockMvc.perform(get("/api/scheduled-classes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lecturer").value(hasItem(DEFAULT_LECTURER.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    @Transactional
    public void getScheduledClass() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get the scheduledClass
        restScheduledClassMockMvc.perform(get("/api/scheduled-classes/{id}", scheduledClass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scheduledClass.getId().intValue()))
            .andExpect(jsonPath("$.courseName").value(DEFAULT_COURSE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.lecturer").value(DEFAULT_LECTURER.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByCourseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where courseName equals to DEFAULT_COURSE_NAME
        defaultScheduledClassShouldBeFound("courseName.equals=" + DEFAULT_COURSE_NAME);

        // Get all the scheduledClassList where courseName equals to UPDATED_COURSE_NAME
        defaultScheduledClassShouldNotBeFound("courseName.equals=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByCourseNameIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where courseName in DEFAULT_COURSE_NAME or UPDATED_COURSE_NAME
        defaultScheduledClassShouldBeFound("courseName.in=" + DEFAULT_COURSE_NAME + "," + UPDATED_COURSE_NAME);

        // Get all the scheduledClassList where courseName equals to UPDATED_COURSE_NAME
        defaultScheduledClassShouldNotBeFound("courseName.in=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByCourseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where courseName is not null
        defaultScheduledClassShouldBeFound("courseName.specified=true");

        // Get all the scheduledClassList where courseName is null
        defaultScheduledClassShouldNotBeFound("courseName.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where description equals to DEFAULT_DESCRIPTION
        defaultScheduledClassShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the scheduledClassList where description equals to UPDATED_DESCRIPTION
        defaultScheduledClassShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultScheduledClassShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the scheduledClassList where description equals to UPDATED_DESCRIPTION
        defaultScheduledClassShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where description is not null
        defaultScheduledClassShouldBeFound("description.specified=true");

        // Get all the scheduledClassList where description is null
        defaultScheduledClassShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByLecturerIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where lecturer equals to DEFAULT_LECTURER
        defaultScheduledClassShouldBeFound("lecturer.equals=" + DEFAULT_LECTURER);

        // Get all the scheduledClassList where lecturer equals to UPDATED_LECTURER
        defaultScheduledClassShouldNotBeFound("lecturer.equals=" + UPDATED_LECTURER);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByLecturerIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where lecturer in DEFAULT_LECTURER or UPDATED_LECTURER
        defaultScheduledClassShouldBeFound("lecturer.in=" + DEFAULT_LECTURER + "," + UPDATED_LECTURER);

        // Get all the scheduledClassList where lecturer equals to UPDATED_LECTURER
        defaultScheduledClassShouldNotBeFound("lecturer.in=" + UPDATED_LECTURER);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByLecturerIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where lecturer is not null
        defaultScheduledClassShouldBeFound("lecturer.specified=true");

        // Get all the scheduledClassList where lecturer is null
        defaultScheduledClassShouldNotBeFound("lecturer.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where location equals to DEFAULT_LOCATION
        defaultScheduledClassShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the scheduledClassList where location equals to UPDATED_LOCATION
        defaultScheduledClassShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultScheduledClassShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the scheduledClassList where location equals to UPDATED_LOCATION
        defaultScheduledClassShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where location is not null
        defaultScheduledClassShouldBeFound("location.specified=true");

        // Get all the scheduledClassList where location is null
        defaultScheduledClassShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where date equals to DEFAULT_DATE
        defaultScheduledClassShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the scheduledClassList where date equals to UPDATED_DATE
        defaultScheduledClassShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where date in DEFAULT_DATE or UPDATED_DATE
        defaultScheduledClassShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the scheduledClassList where date equals to UPDATED_DATE
        defaultScheduledClassShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where date is not null
        defaultScheduledClassShouldBeFound("date.specified=true");

        // Get all the scheduledClassList where date is null
        defaultScheduledClassShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where date greater than or equals to DEFAULT_DATE
        defaultScheduledClassShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the scheduledClassList where date greater than or equals to UPDATED_DATE
        defaultScheduledClassShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllScheduledClassesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledClassRepository.saveAndFlush(scheduledClass);

        // Get all the scheduledClassList where date less than or equals to DEFAULT_DATE
        defaultScheduledClassShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the scheduledClassList where date less than or equals to UPDATED_DATE
        defaultScheduledClassShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultScheduledClassShouldBeFound(String filter) throws Exception {
        restScheduledClassMockMvc.perform(get("/api/scheduled-classes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledClass.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lecturer").value(hasItem(DEFAULT_LECTURER.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultScheduledClassShouldNotBeFound(String filter) throws Exception {
        restScheduledClassMockMvc.perform(get("/api/scheduled-classes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingScheduledClass() throws Exception {
        // Get the scheduledClass
        restScheduledClassMockMvc.perform(get("/api/scheduled-classes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduledClass() throws Exception {
        // Initialize the database
        scheduledClassService.save(scheduledClass);

        int databaseSizeBeforeUpdate = scheduledClassRepository.findAll().size();

        // Update the scheduledClass
        ScheduledClass updatedScheduledClass = scheduledClassRepository.findById(scheduledClass.getId()).get();
        // Disconnect from session so that the updates on updatedScheduledClass are not directly saved in db
        em.detach(updatedScheduledClass);
        updatedScheduledClass
            .courseName(UPDATED_COURSE_NAME)
            .description(UPDATED_DESCRIPTION)
            .lecturer(UPDATED_LECTURER)
            .location(UPDATED_LOCATION)
            .date(UPDATED_DATE);

        restScheduledClassMockMvc.perform(put("/api/scheduled-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScheduledClass)))
            .andExpect(status().isOk());

        // Validate the ScheduledClass in the database
        List<ScheduledClass> scheduledClassList = scheduledClassRepository.findAll();
        assertThat(scheduledClassList).hasSize(databaseSizeBeforeUpdate);
        ScheduledClass testScheduledClass = scheduledClassList.get(scheduledClassList.size() - 1);
        assertThat(testScheduledClass.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testScheduledClass.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testScheduledClass.getLecturer()).isEqualTo(UPDATED_LECTURER);
        assertThat(testScheduledClass.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testScheduledClass.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingScheduledClass() throws Exception {
        int databaseSizeBeforeUpdate = scheduledClassRepository.findAll().size();

        // Create the ScheduledClass

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledClassMockMvc.perform(put("/api/scheduled-classes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledClass)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduledClass in the database
        List<ScheduledClass> scheduledClassList = scheduledClassRepository.findAll();
        assertThat(scheduledClassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteScheduledClass() throws Exception {
        // Initialize the database
        scheduledClassService.save(scheduledClass);

        int databaseSizeBeforeDelete = scheduledClassRepository.findAll().size();

        // Get the scheduledClass
        restScheduledClassMockMvc.perform(delete("/api/scheduled-classes/{id}", scheduledClass.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ScheduledClass> scheduledClassList = scheduledClassRepository.findAll();
        assertThat(scheduledClassList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduledClass.class);
        ScheduledClass scheduledClass1 = new ScheduledClass();
        scheduledClass1.setId(1L);
        ScheduledClass scheduledClass2 = new ScheduledClass();
        scheduledClass2.setId(scheduledClass1.getId());
        assertThat(scheduledClass1).isEqualTo(scheduledClass2);
        scheduledClass2.setId(2L);
        assertThat(scheduledClass1).isNotEqualTo(scheduledClass2);
        scheduledClass1.setId(null);
        assertThat(scheduledClass1).isNotEqualTo(scheduledClass2);
    }
}
