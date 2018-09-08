package mk.ukim.finki.publictransport.web.rest;

import mk.ukim.finki.publictransport.PublictransportApp;

import mk.ukim.finki.publictransport.domain.Line;
import mk.ukim.finki.publictransport.repository.LineRepository;
import mk.ukim.finki.publictransport.service.LineService;
import mk.ukim.finki.publictransport.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.publictransport.service.dto.LineCriteria;
import mk.ukim.finki.publictransport.service.LineQueryService;

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


import static mk.ukim.finki.publictransport.web.rest.TestUtil.sameInstant;
import static mk.ukim.finki.publictransport.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the LineResource REST controller.
 *
 * @see LineResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PublictransportApp.class)
public class LineResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_UNIVERSITY_STOP_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UNIVERSITY_STOP_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private LineRepository lineRepository;
    
    @Autowired
    private LineService lineService;

    @Autowired
    private LineQueryService lineQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restLineMockMvc;

    private Line line;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LineResource lineResource = new LineResource(lineService, lineQueryService);
        this.restLineMockMvc = MockMvcBuilders.standaloneSetup(lineResource)
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
    public static Line createEntity(EntityManager em) {
        Line line = new Line()
            .name(DEFAULT_NAME)
            .universityStopTime(DEFAULT_UNIVERSITY_STOP_TIME);
        return line;
    }

    @Before
    public void initTest() {
        line = createEntity(em);
    }

    @Test
    @Transactional
    public void createLine() throws Exception {
        int databaseSizeBeforeCreate = lineRepository.findAll().size();

        // Create the Line
        restLineMockMvc.perform(post("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(line)))
            .andExpect(status().isCreated());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeCreate + 1);
        Line testLine = lineList.get(lineList.size() - 1);
        assertThat(testLine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLine.getUniversityStopTime()).isEqualTo(DEFAULT_UNIVERSITY_STOP_TIME);
    }

    @Test
    @Transactional
    public void createLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = lineRepository.findAll().size();

        // Create the Line with an existing ID
        line.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLineMockMvc.perform(post("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(line)))
            .andExpect(status().isBadRequest());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllLines() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList
        restLineMockMvc.perform(get("/api/lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(line.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].universityStopTime").value(hasItem(sameInstant(DEFAULT_UNIVERSITY_STOP_TIME))));
    }
    
    @Test
    @Transactional
    public void getLine() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get the line
        restLineMockMvc.perform(get("/api/lines/{id}", line.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(line.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.universityStopTime").value(sameInstant(DEFAULT_UNIVERSITY_STOP_TIME)));
    }

    @Test
    @Transactional
    public void getAllLinesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where name equals to DEFAULT_NAME
        defaultLineShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the lineList where name equals to UPDATED_NAME
        defaultLineShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLinesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLineShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the lineList where name equals to UPDATED_NAME
        defaultLineShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLinesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where name is not null
        defaultLineShouldBeFound("name.specified=true");

        // Get all the lineList where name is null
        defaultLineShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllLinesByUniversityStopTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where universityStopTime equals to DEFAULT_UNIVERSITY_STOP_TIME
        defaultLineShouldBeFound("universityStopTime.equals=" + DEFAULT_UNIVERSITY_STOP_TIME);

        // Get all the lineList where universityStopTime equals to UPDATED_UNIVERSITY_STOP_TIME
        defaultLineShouldNotBeFound("universityStopTime.equals=" + UPDATED_UNIVERSITY_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllLinesByUniversityStopTimeIsInShouldWork() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where universityStopTime in DEFAULT_UNIVERSITY_STOP_TIME or UPDATED_UNIVERSITY_STOP_TIME
        defaultLineShouldBeFound("universityStopTime.in=" + DEFAULT_UNIVERSITY_STOP_TIME + "," + UPDATED_UNIVERSITY_STOP_TIME);

        // Get all the lineList where universityStopTime equals to UPDATED_UNIVERSITY_STOP_TIME
        defaultLineShouldNotBeFound("universityStopTime.in=" + UPDATED_UNIVERSITY_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllLinesByUniversityStopTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where universityStopTime is not null
        defaultLineShouldBeFound("universityStopTime.specified=true");

        // Get all the lineList where universityStopTime is null
        defaultLineShouldNotBeFound("universityStopTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllLinesByUniversityStopTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where universityStopTime greater than or equals to DEFAULT_UNIVERSITY_STOP_TIME
        defaultLineShouldBeFound("universityStopTime.greaterOrEqualThan=" + DEFAULT_UNIVERSITY_STOP_TIME);

        // Get all the lineList where universityStopTime greater than or equals to UPDATED_UNIVERSITY_STOP_TIME
        defaultLineShouldNotBeFound("universityStopTime.greaterOrEqualThan=" + UPDATED_UNIVERSITY_STOP_TIME);
    }

    @Test
    @Transactional
    public void getAllLinesByUniversityStopTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        lineRepository.saveAndFlush(line);

        // Get all the lineList where universityStopTime less than or equals to DEFAULT_UNIVERSITY_STOP_TIME
        defaultLineShouldNotBeFound("universityStopTime.lessThan=" + DEFAULT_UNIVERSITY_STOP_TIME);

        // Get all the lineList where universityStopTime less than or equals to UPDATED_UNIVERSITY_STOP_TIME
        defaultLineShouldBeFound("universityStopTime.lessThan=" + UPDATED_UNIVERSITY_STOP_TIME);
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultLineShouldBeFound(String filter) throws Exception {
        restLineMockMvc.perform(get("/api/lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(line.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].universityStopTime").value(hasItem(sameInstant(DEFAULT_UNIVERSITY_STOP_TIME))));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultLineShouldNotBeFound(String filter) throws Exception {
        restLineMockMvc.perform(get("/api/lines?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingLine() throws Exception {
        // Get the line
        restLineMockMvc.perform(get("/api/lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLine() throws Exception {
        // Initialize the database
        lineService.save(line);

        int databaseSizeBeforeUpdate = lineRepository.findAll().size();

        // Update the line
        Line updatedLine = lineRepository.findById(line.getId()).get();
        // Disconnect from session so that the updates on updatedLine are not directly saved in db
        em.detach(updatedLine);
        updatedLine
            .name(UPDATED_NAME)
            .universityStopTime(UPDATED_UNIVERSITY_STOP_TIME);

        restLineMockMvc.perform(put("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLine)))
            .andExpect(status().isOk());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
        Line testLine = lineList.get(lineList.size() - 1);
        assertThat(testLine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLine.getUniversityStopTime()).isEqualTo(UPDATED_UNIVERSITY_STOP_TIME);
    }

    @Test
    @Transactional
    public void updateNonExistingLine() throws Exception {
        int databaseSizeBeforeUpdate = lineRepository.findAll().size();

        // Create the Line

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLineMockMvc.perform(put("/api/lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(line)))
            .andExpect(status().isBadRequest());

        // Validate the Line in the database
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLine() throws Exception {
        // Initialize the database
        lineService.save(line);

        int databaseSizeBeforeDelete = lineRepository.findAll().size();

        // Get the line
        restLineMockMvc.perform(delete("/api/lines/{id}", line.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Line> lineList = lineRepository.findAll();
        assertThat(lineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Line.class);
        Line line1 = new Line();
        line1.setId(1L);
        Line line2 = new Line();
        line2.setId(line1.getId());
        assertThat(line1).isEqualTo(line2);
        line2.setId(2L);
        assertThat(line1).isNotEqualTo(line2);
        line1.setId(null);
        assertThat(line1).isNotEqualTo(line2);
    }
}
