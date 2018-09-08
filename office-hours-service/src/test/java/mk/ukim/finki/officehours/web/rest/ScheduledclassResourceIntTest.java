package mk.ukim.finki.officehours.web.rest;

import mk.ukim.finki.officehours.OfficehoursApp;

import mk.ukim.finki.officehours.domain.Scheduledclass;
import mk.ukim.finki.officehours.repository.ScheduledclassRepository;
import mk.ukim.finki.officehours.service.ScheduledclassService;
import mk.ukim.finki.officehours.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.officehours.service.dto.ScheduledclassCriteria;
import mk.ukim.finki.officehours.service.ScheduledclassQueryService;

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


import static mk.ukim.finki.officehours.web.rest.TestUtil.sameInstant;
import static mk.ukim.finki.officehours.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import mk.ukim.finki.officehours.domain.enumeration.WeekDay;
/**
 * Test class for the ScheduledclassResource REST controller.
 *
 * @see ScheduledclassResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OfficehoursApp.class)
public class ScheduledclassResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LECTURER = "AAAAAAAAAA";
    private static final String UPDATED_LECTURER = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final WeekDay DEFAULT_DAY = WeekDay.MONDAY;
    private static final WeekDay UPDATED_DAY = WeekDay.TUESDAY;

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ScheduledclassRepository scheduledclassRepository;
    
    @Autowired
    private ScheduledclassService scheduledclassService;

    @Autowired
    private ScheduledclassQueryService scheduledclassQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restScheduledclassMockMvc;

    private Scheduledclass scheduledclass;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScheduledclassResource scheduledclassResource = new ScheduledclassResource(scheduledclassService, scheduledclassQueryService);
        this.restScheduledclassMockMvc = MockMvcBuilders.standaloneSetup(scheduledclassResource)
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
    public static Scheduledclass createEntity(EntityManager em) {
        Scheduledclass scheduledclass = new Scheduledclass()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .lecturer(DEFAULT_LECTURER)
            .location(DEFAULT_LOCATION)
            .day(DEFAULT_DAY)
            .date(DEFAULT_DATE);
        return scheduledclass;
    }

    @Before
    public void initTest() {
        scheduledclass = createEntity(em);
    }

    @Test
    @Transactional
    public void createScheduledclass() throws Exception {
        int databaseSizeBeforeCreate = scheduledclassRepository.findAll().size();

        // Create the Scheduledclass
        restScheduledclassMockMvc.perform(post("/api/scheduledclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledclass)))
            .andExpect(status().isCreated());

        // Validate the Scheduledclass in the database
        List<Scheduledclass> scheduledclassList = scheduledclassRepository.findAll();
        assertThat(scheduledclassList).hasSize(databaseSizeBeforeCreate + 1);
        Scheduledclass testScheduledclass = scheduledclassList.get(scheduledclassList.size() - 1);
        assertThat(testScheduledclass.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScheduledclass.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testScheduledclass.getLecturer()).isEqualTo(DEFAULT_LECTURER);
        assertThat(testScheduledclass.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testScheduledclass.getDay()).isEqualTo(DEFAULT_DAY);
        assertThat(testScheduledclass.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createScheduledclassWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scheduledclassRepository.findAll().size();

        // Create the Scheduledclass with an existing ID
        scheduledclass.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduledclassMockMvc.perform(post("/api/scheduledclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledclass)))
            .andExpect(status().isBadRequest());

        // Validate the Scheduledclass in the database
        List<Scheduledclass> scheduledclassList = scheduledclassRepository.findAll();
        assertThat(scheduledclassList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkDayIsRequired() throws Exception {
        int databaseSizeBeforeTest = scheduledclassRepository.findAll().size();
        // set the field null
        scheduledclass.setDay(null);

        // Create the Scheduledclass, which fails.

        restScheduledclassMockMvc.perform(post("/api/scheduledclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledclass)))
            .andExpect(status().isBadRequest());

        List<Scheduledclass> scheduledclassList = scheduledclassRepository.findAll();
        assertThat(scheduledclassList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScheduledclasses() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList
        restScheduledclassMockMvc.perform(get("/api/scheduledclasses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledclass.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lecturer").value(hasItem(DEFAULT_LECTURER.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }
    
    @Test
    @Transactional
    public void getScheduledclass() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get the scheduledclass
        restScheduledclassMockMvc.perform(get("/api/scheduledclasses/{id}", scheduledclass.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scheduledclass.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.lecturer").value(DEFAULT_LECTURER.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.day").value(DEFAULT_DAY.toString()))
            .andExpect(jsonPath("$.date").value(sameInstant(DEFAULT_DATE)));
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where name equals to DEFAULT_NAME
        defaultScheduledclassShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the scheduledclassList where name equals to UPDATED_NAME
        defaultScheduledclassShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where name in DEFAULT_NAME or UPDATED_NAME
        defaultScheduledclassShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the scheduledclassList where name equals to UPDATED_NAME
        defaultScheduledclassShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where name is not null
        defaultScheduledclassShouldBeFound("name.specified=true");

        // Get all the scheduledclassList where name is null
        defaultScheduledclassShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where description equals to DEFAULT_DESCRIPTION
        defaultScheduledclassShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the scheduledclassList where description equals to UPDATED_DESCRIPTION
        defaultScheduledclassShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultScheduledclassShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the scheduledclassList where description equals to UPDATED_DESCRIPTION
        defaultScheduledclassShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where description is not null
        defaultScheduledclassShouldBeFound("description.specified=true");

        // Get all the scheduledclassList where description is null
        defaultScheduledclassShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByLecturerIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where lecturer equals to DEFAULT_LECTURER
        defaultScheduledclassShouldBeFound("lecturer.equals=" + DEFAULT_LECTURER);

        // Get all the scheduledclassList where lecturer equals to UPDATED_LECTURER
        defaultScheduledclassShouldNotBeFound("lecturer.equals=" + UPDATED_LECTURER);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByLecturerIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where lecturer in DEFAULT_LECTURER or UPDATED_LECTURER
        defaultScheduledclassShouldBeFound("lecturer.in=" + DEFAULT_LECTURER + "," + UPDATED_LECTURER);

        // Get all the scheduledclassList where lecturer equals to UPDATED_LECTURER
        defaultScheduledclassShouldNotBeFound("lecturer.in=" + UPDATED_LECTURER);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByLecturerIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where lecturer is not null
        defaultScheduledclassShouldBeFound("lecturer.specified=true");

        // Get all the scheduledclassList where lecturer is null
        defaultScheduledclassShouldNotBeFound("lecturer.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where location equals to DEFAULT_LOCATION
        defaultScheduledclassShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the scheduledclassList where location equals to UPDATED_LOCATION
        defaultScheduledclassShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultScheduledclassShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the scheduledclassList where location equals to UPDATED_LOCATION
        defaultScheduledclassShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where location is not null
        defaultScheduledclassShouldBeFound("location.specified=true");

        // Get all the scheduledclassList where location is null
        defaultScheduledclassShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDayIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where day equals to DEFAULT_DAY
        defaultScheduledclassShouldBeFound("day.equals=" + DEFAULT_DAY);

        // Get all the scheduledclassList where day equals to UPDATED_DAY
        defaultScheduledclassShouldNotBeFound("day.equals=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDayIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where day in DEFAULT_DAY or UPDATED_DAY
        defaultScheduledclassShouldBeFound("day.in=" + DEFAULT_DAY + "," + UPDATED_DAY);

        // Get all the scheduledclassList where day equals to UPDATED_DAY
        defaultScheduledclassShouldNotBeFound("day.in=" + UPDATED_DAY);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where day is not null
        defaultScheduledclassShouldBeFound("day.specified=true");

        // Get all the scheduledclassList where day is null
        defaultScheduledclassShouldNotBeFound("day.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where date equals to DEFAULT_DATE
        defaultScheduledclassShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the scheduledclassList where date equals to UPDATED_DATE
        defaultScheduledclassShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDateIsInShouldWork() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where date in DEFAULT_DATE or UPDATED_DATE
        defaultScheduledclassShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the scheduledclassList where date equals to UPDATED_DATE
        defaultScheduledclassShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where date is not null
        defaultScheduledclassShouldBeFound("date.specified=true");

        // Get all the scheduledclassList where date is null
        defaultScheduledclassShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where date greater than or equals to DEFAULT_DATE
        defaultScheduledclassShouldBeFound("date.greaterOrEqualThan=" + DEFAULT_DATE);

        // Get all the scheduledclassList where date greater than or equals to UPDATED_DATE
        defaultScheduledclassShouldNotBeFound("date.greaterOrEqualThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllScheduledclassesByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduledclassRepository.saveAndFlush(scheduledclass);

        // Get all the scheduledclassList where date less than or equals to DEFAULT_DATE
        defaultScheduledclassShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the scheduledclassList where date less than or equals to UPDATED_DATE
        defaultScheduledclassShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultScheduledclassShouldBeFound(String filter) throws Exception {
        restScheduledclassMockMvc.perform(get("/api/scheduledclasses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledclass.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].lecturer").value(hasItem(DEFAULT_LECTURER.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].day").value(hasItem(DEFAULT_DAY.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(sameInstant(DEFAULT_DATE))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultScheduledclassShouldNotBeFound(String filter) throws Exception {
        restScheduledclassMockMvc.perform(get("/api/scheduledclasses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingScheduledclass() throws Exception {
        // Get the scheduledclass
        restScheduledclassMockMvc.perform(get("/api/scheduledclasses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScheduledclass() throws Exception {
        // Initialize the database
        scheduledclassService.save(scheduledclass);

        int databaseSizeBeforeUpdate = scheduledclassRepository.findAll().size();

        // Update the scheduledclass
        Scheduledclass updatedScheduledclass = scheduledclassRepository.findById(scheduledclass.getId()).get();
        // Disconnect from session so that the updates on updatedScheduledclass are not directly saved in db
        em.detach(updatedScheduledclass);
        updatedScheduledclass
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .lecturer(UPDATED_LECTURER)
            .location(UPDATED_LOCATION)
            .day(UPDATED_DAY)
            .date(UPDATED_DATE);

        restScheduledclassMockMvc.perform(put("/api/scheduledclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedScheduledclass)))
            .andExpect(status().isOk());

        // Validate the Scheduledclass in the database
        List<Scheduledclass> scheduledclassList = scheduledclassRepository.findAll();
        assertThat(scheduledclassList).hasSize(databaseSizeBeforeUpdate);
        Scheduledclass testScheduledclass = scheduledclassList.get(scheduledclassList.size() - 1);
        assertThat(testScheduledclass.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScheduledclass.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testScheduledclass.getLecturer()).isEqualTo(UPDATED_LECTURER);
        assertThat(testScheduledclass.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testScheduledclass.getDay()).isEqualTo(UPDATED_DAY);
        assertThat(testScheduledclass.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingScheduledclass() throws Exception {
        int databaseSizeBeforeUpdate = scheduledclassRepository.findAll().size();

        // Create the Scheduledclass

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledclassMockMvc.perform(put("/api/scheduledclasses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(scheduledclass)))
            .andExpect(status().isBadRequest());

        // Validate the Scheduledclass in the database
        List<Scheduledclass> scheduledclassList = scheduledclassRepository.findAll();
        assertThat(scheduledclassList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteScheduledclass() throws Exception {
        // Initialize the database
        scheduledclassService.save(scheduledclass);

        int databaseSizeBeforeDelete = scheduledclassRepository.findAll().size();

        // Get the scheduledclass
        restScheduledclassMockMvc.perform(delete("/api/scheduledclasses/{id}", scheduledclass.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Scheduledclass> scheduledclassList = scheduledclassRepository.findAll();
        assertThat(scheduledclassList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Scheduledclass.class);
        Scheduledclass scheduledclass1 = new Scheduledclass();
        scheduledclass1.setId(1L);
        Scheduledclass scheduledclass2 = new Scheduledclass();
        scheduledclass2.setId(scheduledclass1.getId());
        assertThat(scheduledclass1).isEqualTo(scheduledclass2);
        scheduledclass2.setId(2L);
        assertThat(scheduledclass1).isNotEqualTo(scheduledclass2);
        scheduledclass1.setId(null);
        assertThat(scheduledclass1).isNotEqualTo(scheduledclass2);
    }
}
