package mk.ukim.finki.courseinfo.web.rest;

import mk.ukim.finki.courseinfo.CourseinfoApp;

import mk.ukim.finki.courseinfo.domain.Course;
import mk.ukim.finki.courseinfo.repository.CourseRepository;
import mk.ukim.finki.courseinfo.service.CourseService;
import mk.ukim.finki.courseinfo.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.courseinfo.service.dto.CourseCriteria;
import mk.ukim.finki.courseinfo.service.CourseQueryService;

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


import static mk.ukim.finki.courseinfo.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CourseResource REST controller.
 *
 * @see CourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CourseinfoApp.class)
public class CourseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_CREDITS = 1;
    private static final Integer UPDATED_CREDITS = 2;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PROFESSOR = "AAAAAAAAAA";
    private static final String UPDATED_PROFESSOR = "BBBBBBBBBB";

    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseQueryService courseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseMockMvc;

    private Course course;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseResource courseResource = new CourseResource(courseService, courseQueryService);
        this.restCourseMockMvc = MockMvcBuilders.standaloneSetup(courseResource)
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
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .name(DEFAULT_NAME)
            .credits(DEFAULT_CREDITS)
            .description(DEFAULT_DESCRIPTION)
            .professor(DEFAULT_PROFESSOR);
        return course;
    }

    @Before
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getCredits()).isEqualTo(DEFAULT_CREDITS);
        assertThat(testCourse.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCourse.getProfessor()).isEqualTo(DEFAULT_PROFESSOR);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].professor").value(hasItem(DEFAULT_PROFESSOR.toString())));
    }
    
    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.credits").value(DEFAULT_CREDITS))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.professor").value(DEFAULT_PROFESSOR.toString()));
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name equals to DEFAULT_NAME
        defaultCourseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCourseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the courseList where name equals to UPDATED_NAME
        defaultCourseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCoursesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where name is not null
        defaultCourseShouldBeFound("name.specified=true");

        // Get all the courseList where name is null
        defaultCourseShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByCreditsIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where credits equals to DEFAULT_CREDITS
        defaultCourseShouldBeFound("credits.equals=" + DEFAULT_CREDITS);

        // Get all the courseList where credits equals to UPDATED_CREDITS
        defaultCourseShouldNotBeFound("credits.equals=" + UPDATED_CREDITS);
    }

    @Test
    @Transactional
    public void getAllCoursesByCreditsIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where credits in DEFAULT_CREDITS or UPDATED_CREDITS
        defaultCourseShouldBeFound("credits.in=" + DEFAULT_CREDITS + "," + UPDATED_CREDITS);

        // Get all the courseList where credits equals to UPDATED_CREDITS
        defaultCourseShouldNotBeFound("credits.in=" + UPDATED_CREDITS);
    }

    @Test
    @Transactional
    public void getAllCoursesByCreditsIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where credits is not null
        defaultCourseShouldBeFound("credits.specified=true");

        // Get all the courseList where credits is null
        defaultCourseShouldNotBeFound("credits.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByCreditsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where credits greater than or equals to DEFAULT_CREDITS
        defaultCourseShouldBeFound("credits.greaterOrEqualThan=" + DEFAULT_CREDITS);

        // Get all the courseList where credits greater than or equals to UPDATED_CREDITS
        defaultCourseShouldNotBeFound("credits.greaterOrEqualThan=" + UPDATED_CREDITS);
    }

    @Test
    @Transactional
    public void getAllCoursesByCreditsIsLessThanSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where credits less than or equals to DEFAULT_CREDITS
        defaultCourseShouldNotBeFound("credits.lessThan=" + DEFAULT_CREDITS);

        // Get all the courseList where credits less than or equals to UPDATED_CREDITS
        defaultCourseShouldBeFound("credits.lessThan=" + UPDATED_CREDITS);
    }


    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description equals to DEFAULT_DESCRIPTION
        defaultCourseShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultCourseShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the courseList where description equals to UPDATED_DESCRIPTION
        defaultCourseShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllCoursesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where description is not null
        defaultCourseShouldBeFound("description.specified=true");

        // Get all the courseList where description is null
        defaultCourseShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllCoursesByProfessorIsEqualToSomething() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where professor equals to DEFAULT_PROFESSOR
        defaultCourseShouldBeFound("professor.equals=" + DEFAULT_PROFESSOR);

        // Get all the courseList where professor equals to UPDATED_PROFESSOR
        defaultCourseShouldNotBeFound("professor.equals=" + UPDATED_PROFESSOR);
    }

    @Test
    @Transactional
    public void getAllCoursesByProfessorIsInShouldWork() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where professor in DEFAULT_PROFESSOR or UPDATED_PROFESSOR
        defaultCourseShouldBeFound("professor.in=" + DEFAULT_PROFESSOR + "," + UPDATED_PROFESSOR);

        // Get all the courseList where professor equals to UPDATED_PROFESSOR
        defaultCourseShouldNotBeFound("professor.in=" + UPDATED_PROFESSOR);
    }

    @Test
    @Transactional
    public void getAllCoursesByProfessorIsNullOrNotNull() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList where professor is not null
        defaultCourseShouldBeFound("professor.specified=true");

        // Get all the courseList where professor is null
        defaultCourseShouldNotBeFound("professor.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCourseShouldBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].credits").value(hasItem(DEFAULT_CREDITS)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].professor").value(hasItem(DEFAULT_PROFESSOR.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCourseShouldNotBeFound(String filter) throws Exception {
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCourse() throws Exception {
        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .name(UPDATED_NAME)
            .credits(UPDATED_CREDITS)
            .description(UPDATED_DESCRIPTION)
            .professor(UPDATED_PROFESSOR);

        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getCredits()).isEqualTo(UPDATED_CREDITS);
        assertThat(testCourse.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCourse.getProfessor()).isEqualTo(UPDATED_PROFESSOR);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Create the Course

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
        courseService.save(course);

        int databaseSizeBeforeDelete = courseRepository.findAll().size();

        // Get the course
        restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = new Course();
        course1.setId(1L);
        Course course2 = new Course();
        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);
        course2.setId(2L);
        assertThat(course1).isNotEqualTo(course2);
        course1.setId(null);
        assertThat(course1).isNotEqualTo(course2);
    }
}
