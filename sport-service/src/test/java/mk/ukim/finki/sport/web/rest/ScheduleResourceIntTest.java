package mk.ukim.finki.sport.web.rest;

import mk.ukim.finki.sport.SportApp;

import mk.ukim.finki.sport.domain.Schedule;
import mk.ukim.finki.sport.domain.Team;
import mk.ukim.finki.sport.repository.ScheduleRepository;
import mk.ukim.finki.sport.service.ScheduleService;
import mk.ukim.finki.sport.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.sport.service.dto.ScheduleCriteria;
import mk.ukim.finki.sport.service.ScheduleQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static mk.ukim.finki.sport.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ScheduleResource REST controller.
 *
 * @see ScheduleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SportApp.class)
public class ScheduleResourceIntTest {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_SCHEDULED_TIME = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SCHEDULED_TIME = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private ScheduleRepository scheduleRepository;
    
    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ScheduleQueryService scheduleQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restScheduleMockMvc;

    private Schedule schedule;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ScheduleResource scheduleResource = new ScheduleResource(scheduleService, scheduleQueryService);
        this.restScheduleMockMvc = MockMvcBuilders.standaloneSetup(scheduleResource)
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
    public static Schedule createEntity(EntityManager em) {
        Schedule schedule = new Schedule()
            .description(DEFAULT_DESCRIPTION)
            .scheduledTime(DEFAULT_SCHEDULED_TIME);
        return schedule;
    }

    @Before
    public void initTest() {
        schedule = createEntity(em);
    }

    @Test
    @Transactional
    public void createSchedule() throws Exception {
        int databaseSizeBeforeCreate = scheduleRepository.findAll().size();

        // Create the Schedule
        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isCreated());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeCreate + 1);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSchedule.getScheduledTime()).isEqualTo(DEFAULT_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    public void createScheduleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scheduleRepository.findAll().size();

        // Create the Schedule with an existing ID
        schedule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleMockMvc.perform(post("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSchedules() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList
        restScheduleMockMvc.perform(get("/api/schedules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].scheduledTime").value(hasItem(DEFAULT_SCHEDULED_TIME.toString())));
    }
    
    @Test
    @Transactional
    public void getSchedule() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", schedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(schedule.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.scheduledTime").value(DEFAULT_SCHEDULED_TIME.toString()));
    }

    @Test
    @Transactional
    public void getAllSchedulesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where description equals to DEFAULT_DESCRIPTION
        defaultScheduleShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the scheduleList where description equals to UPDATED_DESCRIPTION
        defaultScheduleShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSchedulesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultScheduleShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the scheduleList where description equals to UPDATED_DESCRIPTION
        defaultScheduleShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllSchedulesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where description is not null
        defaultScheduleShouldBeFound("description.specified=true");

        // Get all the scheduleList where description is null
        defaultScheduleShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllSchedulesByScheduledTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where scheduledTime equals to DEFAULT_SCHEDULED_TIME
        defaultScheduleShouldBeFound("scheduledTime.equals=" + DEFAULT_SCHEDULED_TIME);

        // Get all the scheduleList where scheduledTime equals to UPDATED_SCHEDULED_TIME
        defaultScheduleShouldNotBeFound("scheduledTime.equals=" + UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    public void getAllSchedulesByScheduledTimeIsInShouldWork() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where scheduledTime in DEFAULT_SCHEDULED_TIME or UPDATED_SCHEDULED_TIME
        defaultScheduleShouldBeFound("scheduledTime.in=" + DEFAULT_SCHEDULED_TIME + "," + UPDATED_SCHEDULED_TIME);

        // Get all the scheduleList where scheduledTime equals to UPDATED_SCHEDULED_TIME
        defaultScheduleShouldNotBeFound("scheduledTime.in=" + UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    public void getAllSchedulesByScheduledTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where scheduledTime is not null
        defaultScheduleShouldBeFound("scheduledTime.specified=true");

        // Get all the scheduleList where scheduledTime is null
        defaultScheduleShouldNotBeFound("scheduledTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllSchedulesByScheduledTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where scheduledTime greater than or equals to DEFAULT_SCHEDULED_TIME
        defaultScheduleShouldBeFound("scheduledTime.greaterOrEqualThan=" + DEFAULT_SCHEDULED_TIME);

        // Get all the scheduleList where scheduledTime greater than or equals to UPDATED_SCHEDULED_TIME
        defaultScheduleShouldNotBeFound("scheduledTime.greaterOrEqualThan=" + UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    public void getAllSchedulesByScheduledTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where scheduledTime less than or equals to DEFAULT_SCHEDULED_TIME
        defaultScheduleShouldNotBeFound("scheduledTime.lessThan=" + DEFAULT_SCHEDULED_TIME);

        // Get all the scheduleList where scheduledTime less than or equals to UPDATED_SCHEDULED_TIME
        defaultScheduleShouldBeFound("scheduledTime.lessThan=" + UPDATED_SCHEDULED_TIME);
    }


    @Test
    @Transactional
    public void getAllSchedulesByTeamIsEqualToSomething() throws Exception {
        // Initialize the database
        Team team = TeamResourceIntTest.createEntity(em);
        em.persist(team);
        em.flush();
        schedule.addTeam(team);
        scheduleRepository.saveAndFlush(schedule);
        Long teamId = team.getId();

        // Get all the scheduleList where team equals to teamId
        defaultScheduleShouldBeFound("teamId.equals=" + teamId);

        // Get all the scheduleList where team equals to teamId + 1
        defaultScheduleShouldNotBeFound("teamId.equals=" + (teamId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultScheduleShouldBeFound(String filter) throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].scheduledTime").value(hasItem(DEFAULT_SCHEDULED_TIME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultScheduleShouldNotBeFound(String filter) throws Exception {
        restScheduleMockMvc.perform(get("/api/schedules?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingSchedule() throws Exception {
        // Get the schedule
        restScheduleMockMvc.perform(get("/api/schedules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSchedule() throws Exception {
        // Initialize the database
        scheduleService.save(schedule);

        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Update the schedule
        Schedule updatedSchedule = scheduleRepository.findById(schedule.getId()).get();
        // Disconnect from session so that the updates on updatedSchedule are not directly saved in db
        em.detach(updatedSchedule);
        updatedSchedule
            .description(UPDATED_DESCRIPTION)
            .scheduledTime(UPDATED_SCHEDULED_TIME);

        restScheduleMockMvc.perform(put("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSchedule)))
            .andExpect(status().isOk());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
        Schedule testSchedule = scheduleList.get(scheduleList.size() - 1);
        assertThat(testSchedule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSchedule.getScheduledTime()).isEqualTo(UPDATED_SCHEDULED_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingSchedule() throws Exception {
        int databaseSizeBeforeUpdate = scheduleRepository.findAll().size();

        // Create the Schedule

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleMockMvc.perform(put("/api/schedules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(schedule)))
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSchedule() throws Exception {
        // Initialize the database
        scheduleService.save(schedule);

        int databaseSizeBeforeDelete = scheduleRepository.findAll().size();

        // Get the schedule
        restScheduleMockMvc.perform(delete("/api/schedules/{id}", schedule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Schedule> scheduleList = scheduleRepository.findAll();
        assertThat(scheduleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schedule.class);
        Schedule schedule1 = new Schedule();
        schedule1.setId(1L);
        Schedule schedule2 = new Schedule();
        schedule2.setId(schedule1.getId());
        assertThat(schedule1).isEqualTo(schedule2);
        schedule2.setId(2L);
        assertThat(schedule1).isNotEqualTo(schedule2);
        schedule1.setId(null);
        assertThat(schedule1).isNotEqualTo(schedule2);
    }
}
