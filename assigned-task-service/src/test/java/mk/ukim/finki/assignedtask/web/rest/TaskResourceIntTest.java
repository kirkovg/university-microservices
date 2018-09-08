package mk.ukim.finki.assignedtask.web.rest;

import mk.ukim.finki.assignedtask.AssignedtaskApp;

import mk.ukim.finki.assignedtask.domain.Task;
import mk.ukim.finki.assignedtask.repository.TaskRepository;
import mk.ukim.finki.assignedtask.service.TaskService;
import mk.ukim.finki.assignedtask.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.assignedtask.service.dto.TaskCriteria;
import mk.ukim.finki.assignedtask.service.TaskQueryService;

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


import static mk.ukim.finki.assignedtask.web.rest.TestUtil.sameInstant;
import static mk.ukim.finki.assignedtask.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TaskResource REST controller.
 *
 * @see TaskResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssignedtaskApp.class)
public class TaskResourceIntTest {

    private static final String DEFAULT_COURSE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DUE_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DUE_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskQueryService taskQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTaskMockMvc;

    private Task task;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskResource taskResource = new TaskResource(taskService, taskQueryService);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
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
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .courseName(DEFAULT_COURSE_NAME)
            .description(DEFAULT_DESCRIPTION)
            .dueDate(DEFAULT_DUE_DATE);
        return task;
    }

    @Before
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getCourseName()).isEqualTo(DEFAULT_COURSE_NAME);
        assertThat(testTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTask.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    public void createTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task with an existing ID
        task.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(sameInstant(DEFAULT_DUE_DATE))));
    }
    
    @Test
    @Transactional
    public void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.courseName").value(DEFAULT_COURSE_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.dueDate").value(sameInstant(DEFAULT_DUE_DATE)));
    }

    @Test
    @Transactional
    public void getAllTasksByCourseNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where courseName equals to DEFAULT_COURSE_NAME
        defaultTaskShouldBeFound("courseName.equals=" + DEFAULT_COURSE_NAME);

        // Get all the taskList where courseName equals to UPDATED_COURSE_NAME
        defaultTaskShouldNotBeFound("courseName.equals=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByCourseNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where courseName in DEFAULT_COURSE_NAME or UPDATED_COURSE_NAME
        defaultTaskShouldBeFound("courseName.in=" + DEFAULT_COURSE_NAME + "," + UPDATED_COURSE_NAME);

        // Get all the taskList where courseName equals to UPDATED_COURSE_NAME
        defaultTaskShouldNotBeFound("courseName.in=" + UPDATED_COURSE_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByCourseNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where courseName is not null
        defaultTaskShouldBeFound("courseName.specified=true");

        // Get all the taskList where courseName is null
        defaultTaskShouldNotBeFound("courseName.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where description equals to DEFAULT_DESCRIPTION
        defaultTaskShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the taskList where description equals to UPDATED_DESCRIPTION
        defaultTaskShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTasksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultTaskShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the taskList where description equals to UPDATED_DESCRIPTION
        defaultTaskShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllTasksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where description is not null
        defaultTaskShouldBeFound("description.specified=true");

        // Get all the taskList where description is null
        defaultTaskShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate equals to DEFAULT_DUE_DATE
        defaultTaskShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate equals to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultTaskShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the taskList where dueDate equals to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate is not null
        defaultTaskShouldBeFound("dueDate.specified=true");

        // Get all the taskList where dueDate is null
        defaultTaskShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate greater than or equals to DEFAULT_DUE_DATE
        defaultTaskShouldBeFound("dueDate.greaterOrEqualThan=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate greater than or equals to UPDATED_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.greaterOrEqualThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllTasksByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where dueDate less than or equals to DEFAULT_DUE_DATE
        defaultTaskShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the taskList where dueDate less than or equals to UPDATED_DUE_DATE
        defaultTaskShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseName").value(hasItem(DEFAULT_COURSE_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(sameInstant(DEFAULT_DUE_DATE))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
        // Initialize the database
        taskService.save(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).get();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .courseName(UPDATED_COURSE_NAME)
            .description(UPDATED_DESCRIPTION)
            .dueDate(UPDATED_DUE_DATE);

        restTaskMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTask)))
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getCourseName()).isEqualTo(UPDATED_COURSE_NAME);
        assertThat(testTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTask.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Create the Task

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        // Initialize the database
        taskService.save(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Get the task
        restTaskMockMvc.perform(delete("/api/tasks/{id}", task.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = new Task();
        task1.setId(1L);
        Task task2 = new Task();
        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);
        task2.setId(2L);
        assertThat(task1).isNotEqualTo(task2);
        task1.setId(null);
        assertThat(task1).isNotEqualTo(task2);
    }
}
