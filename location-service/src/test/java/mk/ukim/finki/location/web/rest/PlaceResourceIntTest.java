package mk.ukim.finki.location.web.rest;

import mk.ukim.finki.location.LocationApp;

import mk.ukim.finki.location.domain.Place;
import mk.ukim.finki.location.repository.PlaceRepository;
import mk.ukim.finki.location.service.PlaceService;
import mk.ukim.finki.location.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.location.service.dto.PlaceCriteria;
import mk.ukim.finki.location.service.PlaceQueryService;

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


import static mk.ukim.finki.location.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlaceResource REST controller.
 *
 * @see PlaceResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LocationApp.class)
public class PlaceResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_LINE = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_LINE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_POS_X = new BigDecimal(1);
    private static final BigDecimal UPDATED_POS_X = new BigDecimal(2);

    private static final BigDecimal DEFAULT_POS_Y = new BigDecimal(1);
    private static final BigDecimal UPDATED_POS_Y = new BigDecimal(2);

    @Autowired
    private PlaceRepository placeRepository;
    
    @Autowired
    private PlaceService placeService;

    @Autowired
    private PlaceQueryService placeQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPlaceMockMvc;

    private Place place;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlaceResource placeResource = new PlaceResource(placeService, placeQueryService);
        this.restPlaceMockMvc = MockMvcBuilders.standaloneSetup(placeResource)
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
    public static Place createEntity(EntityManager em) {
        Place place = new Place()
            .name(DEFAULT_NAME)
            .addressLine(DEFAULT_ADDRESS_LINE)
            .posX(DEFAULT_POS_X)
            .posY(DEFAULT_POS_Y);
        return place;
    }

    @Before
    public void initTest() {
        place = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlace() throws Exception {
        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // Create the Place
        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(place)))
            .andExpect(status().isCreated());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate + 1);
        Place testPlace = placeList.get(placeList.size() - 1);
        assertThat(testPlace.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlace.getAddressLine()).isEqualTo(DEFAULT_ADDRESS_LINE);
        assertThat(testPlace.getPosX()).isEqualTo(DEFAULT_POS_X);
        assertThat(testPlace.getPosY()).isEqualTo(DEFAULT_POS_Y);
    }

    @Test
    @Transactional
    public void createPlaceWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = placeRepository.findAll().size();

        // Create the Place with an existing ID
        place.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlaceMockMvc.perform(post("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(place)))
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPlaces() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList
        restPlaceMockMvc.perform(get("/api/places?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].addressLine").value(hasItem(DEFAULT_ADDRESS_LINE.toString())))
            .andExpect(jsonPath("$.[*].posX").value(hasItem(DEFAULT_POS_X.intValue())))
            .andExpect(jsonPath("$.[*].posY").value(hasItem(DEFAULT_POS_Y.intValue())));
    }
    
    @Test
    @Transactional
    public void getPlace() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get the place
        restPlaceMockMvc.perform(get("/api/places/{id}", place.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(place.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.addressLine").value(DEFAULT_ADDRESS_LINE.toString()))
            .andExpect(jsonPath("$.posX").value(DEFAULT_POS_X.intValue()))
            .andExpect(jsonPath("$.posY").value(DEFAULT_POS_Y.intValue()));
    }

    @Test
    @Transactional
    public void getAllPlacesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name equals to DEFAULT_NAME
        defaultPlaceShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the placeList where name equals to UPDATED_NAME
        defaultPlaceShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlacesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name in DEFAULT_NAME or UPDATED_NAME
        defaultPlaceShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the placeList where name equals to UPDATED_NAME
        defaultPlaceShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllPlacesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where name is not null
        defaultPlaceShouldBeFound("name.specified=true");

        // Get all the placeList where name is null
        defaultPlaceShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlacesByAddressLineIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where addressLine equals to DEFAULT_ADDRESS_LINE
        defaultPlaceShouldBeFound("addressLine.equals=" + DEFAULT_ADDRESS_LINE);

        // Get all the placeList where addressLine equals to UPDATED_ADDRESS_LINE
        defaultPlaceShouldNotBeFound("addressLine.equals=" + UPDATED_ADDRESS_LINE);
    }

    @Test
    @Transactional
    public void getAllPlacesByAddressLineIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where addressLine in DEFAULT_ADDRESS_LINE or UPDATED_ADDRESS_LINE
        defaultPlaceShouldBeFound("addressLine.in=" + DEFAULT_ADDRESS_LINE + "," + UPDATED_ADDRESS_LINE);

        // Get all the placeList where addressLine equals to UPDATED_ADDRESS_LINE
        defaultPlaceShouldNotBeFound("addressLine.in=" + UPDATED_ADDRESS_LINE);
    }

    @Test
    @Transactional
    public void getAllPlacesByAddressLineIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where addressLine is not null
        defaultPlaceShouldBeFound("addressLine.specified=true");

        // Get all the placeList where addressLine is null
        defaultPlaceShouldNotBeFound("addressLine.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlacesByPosXIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where posX equals to DEFAULT_POS_X
        defaultPlaceShouldBeFound("posX.equals=" + DEFAULT_POS_X);

        // Get all the placeList where posX equals to UPDATED_POS_X
        defaultPlaceShouldNotBeFound("posX.equals=" + UPDATED_POS_X);
    }

    @Test
    @Transactional
    public void getAllPlacesByPosXIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where posX in DEFAULT_POS_X or UPDATED_POS_X
        defaultPlaceShouldBeFound("posX.in=" + DEFAULT_POS_X + "," + UPDATED_POS_X);

        // Get all the placeList where posX equals to UPDATED_POS_X
        defaultPlaceShouldNotBeFound("posX.in=" + UPDATED_POS_X);
    }

    @Test
    @Transactional
    public void getAllPlacesByPosXIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where posX is not null
        defaultPlaceShouldBeFound("posX.specified=true");

        // Get all the placeList where posX is null
        defaultPlaceShouldNotBeFound("posX.specified=false");
    }

    @Test
    @Transactional
    public void getAllPlacesByPosYIsEqualToSomething() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where posY equals to DEFAULT_POS_Y
        defaultPlaceShouldBeFound("posY.equals=" + DEFAULT_POS_Y);

        // Get all the placeList where posY equals to UPDATED_POS_Y
        defaultPlaceShouldNotBeFound("posY.equals=" + UPDATED_POS_Y);
    }

    @Test
    @Transactional
    public void getAllPlacesByPosYIsInShouldWork() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where posY in DEFAULT_POS_Y or UPDATED_POS_Y
        defaultPlaceShouldBeFound("posY.in=" + DEFAULT_POS_Y + "," + UPDATED_POS_Y);

        // Get all the placeList where posY equals to UPDATED_POS_Y
        defaultPlaceShouldNotBeFound("posY.in=" + UPDATED_POS_Y);
    }

    @Test
    @Transactional
    public void getAllPlacesByPosYIsNullOrNotNull() throws Exception {
        // Initialize the database
        placeRepository.saveAndFlush(place);

        // Get all the placeList where posY is not null
        defaultPlaceShouldBeFound("posY.specified=true");

        // Get all the placeList where posY is null
        defaultPlaceShouldNotBeFound("posY.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPlaceShouldBeFound(String filter) throws Exception {
        restPlaceMockMvc.perform(get("/api/places?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(place.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].addressLine").value(hasItem(DEFAULT_ADDRESS_LINE.toString())))
            .andExpect(jsonPath("$.[*].posX").value(hasItem(DEFAULT_POS_X.intValue())))
            .andExpect(jsonPath("$.[*].posY").value(hasItem(DEFAULT_POS_Y.intValue())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPlaceShouldNotBeFound(String filter) throws Exception {
        restPlaceMockMvc.perform(get("/api/places?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPlace() throws Exception {
        // Get the place
        restPlaceMockMvc.perform(get("/api/places/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlace() throws Exception {
        // Initialize the database
        placeService.save(place);

        int databaseSizeBeforeUpdate = placeRepository.findAll().size();

        // Update the place
        Place updatedPlace = placeRepository.findById(place.getId()).get();
        // Disconnect from session so that the updates on updatedPlace are not directly saved in db
        em.detach(updatedPlace);
        updatedPlace
            .name(UPDATED_NAME)
            .addressLine(UPDATED_ADDRESS_LINE)
            .posX(UPDATED_POS_X)
            .posY(UPDATED_POS_Y);

        restPlaceMockMvc.perform(put("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlace)))
            .andExpect(status().isOk());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
        Place testPlace = placeList.get(placeList.size() - 1);
        assertThat(testPlace.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlace.getAddressLine()).isEqualTo(UPDATED_ADDRESS_LINE);
        assertThat(testPlace.getPosX()).isEqualTo(UPDATED_POS_X);
        assertThat(testPlace.getPosY()).isEqualTo(UPDATED_POS_Y);
    }

    @Test
    @Transactional
    public void updateNonExistingPlace() throws Exception {
        int databaseSizeBeforeUpdate = placeRepository.findAll().size();

        // Create the Place

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlaceMockMvc.perform(put("/api/places")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(place)))
            .andExpect(status().isBadRequest());

        // Validate the Place in the database
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlace() throws Exception {
        // Initialize the database
        placeService.save(place);

        int databaseSizeBeforeDelete = placeRepository.findAll().size();

        // Get the place
        restPlaceMockMvc.perform(delete("/api/places/{id}", place.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Place> placeList = placeRepository.findAll();
        assertThat(placeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Place.class);
        Place place1 = new Place();
        place1.setId(1L);
        Place place2 = new Place();
        place2.setId(place1.getId());
        assertThat(place1).isEqualTo(place2);
        place2.setId(2L);
        assertThat(place1).isNotEqualTo(place2);
        place1.setId(null);
        assertThat(place1).isNotEqualTo(place2);
    }
}
