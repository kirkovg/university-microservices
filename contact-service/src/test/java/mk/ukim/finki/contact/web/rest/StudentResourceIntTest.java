package mk.ukim.finki.contact.web.rest;

import mk.ukim.finki.contact.ContactApp;

import mk.ukim.finki.contact.domain.Student;
import mk.ukim.finki.contact.repository.StudentRepository;
import mk.ukim.finki.contact.service.StudentService;
import mk.ukim.finki.contact.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.contact.service.dto.StudentCriteria;
import mk.ukim.finki.contact.service.StudentQueryService;

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


import static mk.ukim.finki.contact.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the StudentResource REST controller.
 *
 * @see StudentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ContactApp.class)
public class StudentResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_OTHER_INFO = "AAAAAAAAAA";
    private static final String UPDATED_OTHER_INFO = "BBBBBBBBBB";

    @Autowired
    private StudentRepository studentRepository;
    
    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentQueryService studentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentMockMvc;

    private Student student;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudentResource studentResource = new StudentResource(studentService, studentQueryService);
        this.restStudentMockMvc = MockMvcBuilders.standaloneSetup(studentResource)
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
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .phoneNumber(DEFAULT_PHONE_NUMBER)
            .otherInfo(DEFAULT_OTHER_INFO);
        return student;
    }

    @Before
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student
        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
        assertThat(testStudent.getOtherInfo()).isEqualTo(DEFAULT_OTHER_INFO);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].otherInfo").value(hasItem(DEFAULT_OTHER_INFO.toString())));
    }
    
    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()))
            .andExpect(jsonPath("$.otherInfo").value(DEFAULT_OTHER_INFO.toString()));
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName equals to DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName is not null
        defaultStudentShouldBeFound("firstName.specified=true");

        // Get all the studentList where firstName is null
        defaultStudentShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName equals to DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName is not null
        defaultStudentShouldBeFound("lastName.specified=true");

        // Get all the studentList where lastName is null
        defaultStudentShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email equals to DEFAULT_EMAIL
        defaultStudentShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the studentList where email equals to UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultStudentShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the studentList where email equals to UPDATED_EMAIL
        defaultStudentShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllStudentsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where email is not null
        defaultStudentShouldBeFound("email.specified=true");

        // Get all the studentList where email is null
        defaultStudentShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber equals to DEFAULT_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.equals=" + DEFAULT_PHONE_NUMBER);

        // Get all the studentList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.equals=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneNumberIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber in DEFAULT_PHONE_NUMBER or UPDATED_PHONE_NUMBER
        defaultStudentShouldBeFound("phoneNumber.in=" + DEFAULT_PHONE_NUMBER + "," + UPDATED_PHONE_NUMBER);

        // Get all the studentList where phoneNumber equals to UPDATED_PHONE_NUMBER
        defaultStudentShouldNotBeFound("phoneNumber.in=" + UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllStudentsByPhoneNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where phoneNumber is not null
        defaultStudentShouldBeFound("phoneNumber.specified=true");

        // Get all the studentList where phoneNumber is null
        defaultStudentShouldNotBeFound("phoneNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByOtherInfoIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where otherInfo equals to DEFAULT_OTHER_INFO
        defaultStudentShouldBeFound("otherInfo.equals=" + DEFAULT_OTHER_INFO);

        // Get all the studentList where otherInfo equals to UPDATED_OTHER_INFO
        defaultStudentShouldNotBeFound("otherInfo.equals=" + UPDATED_OTHER_INFO);
    }

    @Test
    @Transactional
    public void getAllStudentsByOtherInfoIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where otherInfo in DEFAULT_OTHER_INFO or UPDATED_OTHER_INFO
        defaultStudentShouldBeFound("otherInfo.in=" + DEFAULT_OTHER_INFO + "," + UPDATED_OTHER_INFO);

        // Get all the studentList where otherInfo equals to UPDATED_OTHER_INFO
        defaultStudentShouldNotBeFound("otherInfo.in=" + UPDATED_OTHER_INFO);
    }

    @Test
    @Transactional
    public void getAllStudentsByOtherInfoIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where otherInfo is not null
        defaultStudentShouldBeFound("otherInfo.specified=true");

        // Get all the studentList where otherInfo is null
        defaultStudentShouldNotBeFound("otherInfo.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
            .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].otherInfo").value(hasItem(DEFAULT_OTHER_INFO.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentService.save(student);

        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findById(student.getId()).get();
        // Disconnect from session so that the updates on updatedStudent are not directly saved in db
        em.detach(updatedStudent);
        updatedStudent
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .phoneNumber(UPDATED_PHONE_NUMBER)
            .otherInfo(UPDATED_OTHER_INFO);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedStudent)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testStudent.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
        assertThat(testStudent.getOtherInfo()).isEqualTo(UPDATED_OTHER_INFO);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Create the Student

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStudentMockMvc.perform(put("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(student)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentService.save(student);

        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Get the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);
        student2.setId(2L);
        assertThat(student1).isNotEqualTo(student2);
        student1.setId(null);
        assertThat(student1).isNotEqualTo(student2);
    }
}
