package mk.ukim.finki.gradespreview.web.rest;

import mk.ukim.finki.gradespreview.GradespreviewApp;

import mk.ukim.finki.gradespreview.domain.CompletedCourse;
import mk.ukim.finki.gradespreview.repository.CompletedCourseRepository;
import mk.ukim.finki.gradespreview.service.CompletedCourseService;
import mk.ukim.finki.gradespreview.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.gradespreview.service.dto.CompletedCourseCriteria;
import mk.ukim.finki.gradespreview.service.CompletedCourseQueryService;

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
import java.math.BigDecimal;
import java.util.List;


import static mk.ukim.finki.gradespreview.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CompletedCourseResource REST controller.
 *
 * @see CompletedCourseResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = GradespreviewApp.class)
public class CompletedCourseResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_GRADE = new BigDecimal(1);
    private static final BigDecimal UPDATED_GRADE = new BigDecimal(2);

    @Autowired
    private CompletedCourseRepository completedCourseRepository;
    
    @Autowired
    private CompletedCourseService completedCourseService;

    @Autowired
    private CompletedCourseQueryService completedCourseQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCompletedCourseMockMvc;

    private CompletedCourse completedCourse;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CompletedCourseResource completedCourseResource = new CompletedCourseResource(completedCourseService, completedCourseQueryService);
        this.restCompletedCourseMockMvc = MockMvcBuilders.standaloneSetup(completedCourseResource)
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
    public static CompletedCourse createEntity(EntityManager em) {
        CompletedCourse completedCourse = new CompletedCourse()
            .name(DEFAULT_NAME)
            .grade(DEFAULT_GRADE);
        return completedCourse;
    }

    @Before
    public void initTest() {
        completedCourse = createEntity(em);
    }

    @Test
    @Transactional
    public void createCompletedCourse() throws Exception {
        int databaseSizeBeforeCreate = completedCourseRepository.findAll().size();

        // Create the CompletedCourse
        restCompletedCourseMockMvc.perform(post("/api/completed-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(completedCourse)))
            .andExpect(status().isCreated());

        // Validate the CompletedCourse in the database
        List<CompletedCourse> completedCourseList = completedCourseRepository.findAll();
        assertThat(completedCourseList).hasSize(databaseSizeBeforeCreate + 1);
        CompletedCourse testCompletedCourse = completedCourseList.get(completedCourseList.size() - 1);
        assertThat(testCompletedCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCompletedCourse.getGrade()).isEqualTo(DEFAULT_GRADE);
    }

    @Test
    @Transactional
    public void createCompletedCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = completedCourseRepository.findAll().size();

        // Create the CompletedCourse with an existing ID
        completedCourse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompletedCourseMockMvc.perform(post("/api/completed-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(completedCourse)))
            .andExpect(status().isBadRequest());

        // Validate the CompletedCourse in the database
        List<CompletedCourse> completedCourseList = completedCourseRepository.findAll();
        assertThat(completedCourseList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCompletedCourses() throws Exception {
        // Initialize the database
        completedCourseRepository.saveAndFlush(completedCourse);

        // Get all the completedCourseList
        restCompletedCourseMockMvc.perform(get("/api/completed-courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(completedCourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.intValue())));
    }
    
    @Test
    @Transactional
    public void getCompletedCourse() throws Exception {
        // Initialize the database
        completedCourseRepository.saveAndFlush(completedCourse);

        // Get the completedCourse
        restCompletedCourseMockMvc.perform(get("/api/completed-courses/{id}", completedCourse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(completedCourse.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.grade").value(DEFAULT_GRADE.intValue()));
    }

    @Test
    @Transactional
    public void getAllCompletedCoursesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        completedCourseRepository.saveAndFlush(completedCourse);

        // Get all the completedCourseList where name equals to DEFAULT_NAME
        defaultCompletedCourseShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the completedCourseList where name equals to UPDATED_NAME
        defaultCompletedCourseShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCompletedCoursesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        completedCourseRepository.saveAndFlush(completedCourse);

        // Get all the completedCourseList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCompletedCourseShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the completedCourseList where name equals to UPDATED_NAME
        defaultCompletedCourseShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCompletedCoursesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        completedCourseRepository.saveAndFlush(completedCourse);

        // Get all the completedCourseList where name is not null
        defaultCompletedCourseShouldBeFound("name.specified=true");

        // Get all the completedCourseList where name is null
        defaultCompletedCourseShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCompletedCoursesByGradeIsEqualToSomething() throws Exception {
        // Initialize the database
        completedCourseRepository.saveAndFlush(completedCourse);

        // Get all the completedCourseList where grade equals to DEFAULT_GRADE
        defaultCompletedCourseShouldBeFound("grade.equals=" + DEFAULT_GRADE);

        // Get all the completedCourseList where grade equals to UPDATED_GRADE
        defaultCompletedCourseShouldNotBeFound("grade.equals=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllCompletedCoursesByGradeIsInShouldWork() throws Exception {
        // Initialize the database
        completedCourseRepository.saveAndFlush(completedCourse);

        // Get all the completedCourseList where grade in DEFAULT_GRADE or UPDATED_GRADE
        defaultCompletedCourseShouldBeFound("grade.in=" + DEFAULT_GRADE + "," + UPDATED_GRADE);

        // Get all the completedCourseList where grade equals to UPDATED_GRADE
        defaultCompletedCourseShouldNotBeFound("grade.in=" + UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void getAllCompletedCoursesByGradeIsNullOrNotNull() throws Exception {
        // Initialize the database
        completedCourseRepository.saveAndFlush(completedCourse);

        // Get all the completedCourseList where grade is not null
        defaultCompletedCourseShouldBeFound("grade.specified=true");

        // Get all the completedCourseList where grade is null
        defaultCompletedCourseShouldNotBeFound("grade.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCompletedCourseShouldBeFound(String filter) throws Exception {
        restCompletedCourseMockMvc.perform(get("/api/completed-courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(completedCourse.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].grade").value(hasItem(DEFAULT_GRADE.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCompletedCourseShouldNotBeFound(String filter) throws Exception {
        restCompletedCourseMockMvc.perform(get("/api/completed-courses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCompletedCourse() throws Exception {
        // Get the completedCourse
        restCompletedCourseMockMvc.perform(get("/api/completed-courses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCompletedCourse() throws Exception {
        // Initialize the database
        completedCourseService.save(completedCourse);

        int databaseSizeBeforeUpdate = completedCourseRepository.findAll().size();

        // Update the completedCourse
        CompletedCourse updatedCompletedCourse = completedCourseRepository.findById(completedCourse.getId()).get();
        // Disconnect from session so that the updates on updatedCompletedCourse are not directly saved in db
        em.detach(updatedCompletedCourse);
        updatedCompletedCourse
            .name(UPDATED_NAME)
            .grade(UPDATED_GRADE);

        restCompletedCourseMockMvc.perform(put("/api/completed-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCompletedCourse)))
            .andExpect(status().isOk());

        // Validate the CompletedCourse in the database
        List<CompletedCourse> completedCourseList = completedCourseRepository.findAll();
        assertThat(completedCourseList).hasSize(databaseSizeBeforeUpdate);
        CompletedCourse testCompletedCourse = completedCourseList.get(completedCourseList.size() - 1);
        assertThat(testCompletedCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCompletedCourse.getGrade()).isEqualTo(UPDATED_GRADE);
    }

    @Test
    @Transactional
    public void updateNonExistingCompletedCourse() throws Exception {
        int databaseSizeBeforeUpdate = completedCourseRepository.findAll().size();

        // Create the CompletedCourse

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompletedCourseMockMvc.perform(put("/api/completed-courses")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(completedCourse)))
            .andExpect(status().isBadRequest());

        // Validate the CompletedCourse in the database
        List<CompletedCourse> completedCourseList = completedCourseRepository.findAll();
        assertThat(completedCourseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCompletedCourse() throws Exception {
        // Initialize the database
        completedCourseService.save(completedCourse);

        int databaseSizeBeforeDelete = completedCourseRepository.findAll().size();

        // Get the completedCourse
        restCompletedCourseMockMvc.perform(delete("/api/completed-courses/{id}", completedCourse.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CompletedCourse> completedCourseList = completedCourseRepository.findAll();
        assertThat(completedCourseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompletedCourse.class);
        CompletedCourse completedCourse1 = new CompletedCourse();
        completedCourse1.setId(1L);
        CompletedCourse completedCourse2 = new CompletedCourse();
        completedCourse2.setId(completedCourse1.getId());
        assertThat(completedCourse1).isEqualTo(completedCourse2);
        completedCourse2.setId(2L);
        assertThat(completedCourse1).isNotEqualTo(completedCourse2);
        completedCourse1.setId(null);
        assertThat(completedCourse1).isNotEqualTo(completedCourse2);
    }
}
