package mk.ukim.finki.communication.web.rest;

import mk.ukim.finki.communication.CommunicationApp;

import mk.ukim.finki.communication.domain.Appuser;
import mk.ukim.finki.communication.repository.AppuserRepository;
import mk.ukim.finki.communication.service.AppuserService;
import mk.ukim.finki.communication.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.communication.service.dto.AppuserCriteria;
import mk.ukim.finki.communication.service.AppuserQueryService;

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


import static mk.ukim.finki.communication.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the AppuserResource REST controller.
 *
 * @see AppuserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommunicationApp.class)
public class AppuserResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private AppuserRepository appuserRepository;
    
    @Autowired
    private AppuserService appuserService;

    @Autowired
    private AppuserQueryService appuserQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAppuserMockMvc;

    private Appuser appuser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AppuserResource appuserResource = new AppuserResource(appuserService, appuserQueryService);
        this.restAppuserMockMvc = MockMvcBuilders.standaloneSetup(appuserResource)
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
    public static Appuser createEntity(EntityManager em) {
        Appuser appuser = new Appuser()
            .name(DEFAULT_NAME);
        return appuser;
    }

    @Before
    public void initTest() {
        appuser = createEntity(em);
    }

    @Test
    @Transactional
    public void createAppuser() throws Exception {
        int databaseSizeBeforeCreate = appuserRepository.findAll().size();

        // Create the Appuser
        restAppuserMockMvc.perform(post("/api/appusers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appuser)))
            .andExpect(status().isCreated());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeCreate + 1);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createAppuserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = appuserRepository.findAll().size();

        // Create the Appuser with an existing ID
        appuser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAppuserMockMvc.perform(post("/api/appusers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appuser)))
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAppusers() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList
        restAppuserMockMvc.perform(get("/api/appusers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getAppuser() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get the appuser
        restAppuserMockMvc.perform(get("/api/appusers/{id}", appuser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(appuser.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getAllAppusersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where name equals to DEFAULT_NAME
        defaultAppuserShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the appuserList where name equals to UPDATED_NAME
        defaultAppuserShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAppusersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where name in DEFAULT_NAME or UPDATED_NAME
        defaultAppuserShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the appuserList where name equals to UPDATED_NAME
        defaultAppuserShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllAppusersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        appuserRepository.saveAndFlush(appuser);

        // Get all the appuserList where name is not null
        defaultAppuserShouldBeFound("name.specified=true");

        // Get all the appuserList where name is null
        defaultAppuserShouldNotBeFound("name.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultAppuserShouldBeFound(String filter) throws Exception {
        restAppuserMockMvc.perform(get("/api/appusers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(appuser.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultAppuserShouldNotBeFound(String filter) throws Exception {
        restAppuserMockMvc.perform(get("/api/appusers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingAppuser() throws Exception {
        // Get the appuser
        restAppuserMockMvc.perform(get("/api/appusers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAppuser() throws Exception {
        // Initialize the database
        appuserService.save(appuser);

        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Update the appuser
        Appuser updatedAppuser = appuserRepository.findById(appuser.getId()).get();
        // Disconnect from session so that the updates on updatedAppuser are not directly saved in db
        em.detach(updatedAppuser);
        updatedAppuser
            .name(UPDATED_NAME);

        restAppuserMockMvc.perform(put("/api/appusers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAppuser)))
            .andExpect(status().isOk());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
        Appuser testAppuser = appuserList.get(appuserList.size() - 1);
        assertThat(testAppuser.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingAppuser() throws Exception {
        int databaseSizeBeforeUpdate = appuserRepository.findAll().size();

        // Create the Appuser

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAppuserMockMvc.perform(put("/api/appusers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(appuser)))
            .andExpect(status().isBadRequest());

        // Validate the Appuser in the database
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAppuser() throws Exception {
        // Initialize the database
        appuserService.save(appuser);

        int databaseSizeBeforeDelete = appuserRepository.findAll().size();

        // Get the appuser
        restAppuserMockMvc.perform(delete("/api/appusers/{id}", appuser.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Appuser> appuserList = appuserRepository.findAll();
        assertThat(appuserList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appuser.class);
        Appuser appuser1 = new Appuser();
        appuser1.setId(1L);
        Appuser appuser2 = new Appuser();
        appuser2.setId(appuser1.getId());
        assertThat(appuser1).isEqualTo(appuser2);
        appuser2.setId(2L);
        assertThat(appuser1).isNotEqualTo(appuser2);
        appuser1.setId(null);
        assertThat(appuser1).isNotEqualTo(appuser2);
    }
}
