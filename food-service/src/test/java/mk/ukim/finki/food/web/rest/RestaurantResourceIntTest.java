package mk.ukim.finki.food.web.rest;

import mk.ukim.finki.food.FoodApp;

import mk.ukim.finki.food.domain.Restaurant;
import mk.ukim.finki.food.domain.Menu;
import mk.ukim.finki.food.repository.RestaurantRepository;
import mk.ukim.finki.food.service.RestaurantService;
import mk.ukim.finki.food.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.food.service.dto.RestaurantCriteria;
import mk.ukim.finki.food.service.RestaurantQueryService;

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


import static mk.ukim.finki.food.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RestaurantResource REST controller.
 *
 * @see RestaurantResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoodApp.class)
public class RestaurantResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Integer DEFAULT_CAPACITY = 1;
    private static final Integer UPDATED_CAPACITY = 2;

    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private RestaurantQueryService restaurantQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restRestaurantMockMvc;

    private Restaurant restaurant;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RestaurantResource restaurantResource = new RestaurantResource(restaurantService, restaurantQueryService);
        this.restRestaurantMockMvc = MockMvcBuilders.standaloneSetup(restaurantResource)
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
    public static Restaurant createEntity(EntityManager em) {
        Restaurant restaurant = new Restaurant()
            .name(DEFAULT_NAME)
            .location(DEFAULT_LOCATION)
            .capacity(DEFAULT_CAPACITY);
        return restaurant;
    }

    @Before
    public void initTest() {
        restaurant = createEntity(em);
    }

    @Test
    @Transactional
    public void createRestaurant() throws Exception {
        int databaseSizeBeforeCreate = restaurantRepository.findAll().size();

        // Create the Restaurant
        restRestaurantMockMvc.perform(post("/api/restaurants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isCreated());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate + 1);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRestaurant.getLocation()).isEqualTo(DEFAULT_LOCATION);
        assertThat(testRestaurant.getCapacity()).isEqualTo(DEFAULT_CAPACITY);
    }

    @Test
    @Transactional
    public void createRestaurantWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = restaurantRepository.findAll().size();

        // Create the Restaurant with an existing ID
        restaurant.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRestaurantMockMvc.perform(post("/api/restaurants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllRestaurants() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList
        restRestaurantMockMvc.perform(get("/api/restaurants?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }
    
    @Test
    @Transactional
    public void getRestaurant() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get the restaurant
        restRestaurantMockMvc.perform(get("/api/restaurants/{id}", restaurant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(restaurant.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION.toString()))
            .andExpect(jsonPath("$.capacity").value(DEFAULT_CAPACITY));
    }

    @Test
    @Transactional
    public void getAllRestaurantsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where name equals to DEFAULT_NAME
        defaultRestaurantShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the restaurantList where name equals to UPDATED_NAME
        defaultRestaurantShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRestaurantsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where name in DEFAULT_NAME or UPDATED_NAME
        defaultRestaurantShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the restaurantList where name equals to UPDATED_NAME
        defaultRestaurantShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllRestaurantsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where name is not null
        defaultRestaurantShouldBeFound("name.specified=true");

        // Get all the restaurantList where name is null
        defaultRestaurantShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestaurantsByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where location equals to DEFAULT_LOCATION
        defaultRestaurantShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the restaurantList where location equals to UPDATED_LOCATION
        defaultRestaurantShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllRestaurantsByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultRestaurantShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the restaurantList where location equals to UPDATED_LOCATION
        defaultRestaurantShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void getAllRestaurantsByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where location is not null
        defaultRestaurantShouldBeFound("location.specified=true");

        // Get all the restaurantList where location is null
        defaultRestaurantShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestaurantsByCapacityIsEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where capacity equals to DEFAULT_CAPACITY
        defaultRestaurantShouldBeFound("capacity.equals=" + DEFAULT_CAPACITY);

        // Get all the restaurantList where capacity equals to UPDATED_CAPACITY
        defaultRestaurantShouldNotBeFound("capacity.equals=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllRestaurantsByCapacityIsInShouldWork() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where capacity in DEFAULT_CAPACITY or UPDATED_CAPACITY
        defaultRestaurantShouldBeFound("capacity.in=" + DEFAULT_CAPACITY + "," + UPDATED_CAPACITY);

        // Get all the restaurantList where capacity equals to UPDATED_CAPACITY
        defaultRestaurantShouldNotBeFound("capacity.in=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllRestaurantsByCapacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where capacity is not null
        defaultRestaurantShouldBeFound("capacity.specified=true");

        // Get all the restaurantList where capacity is null
        defaultRestaurantShouldNotBeFound("capacity.specified=false");
    }

    @Test
    @Transactional
    public void getAllRestaurantsByCapacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where capacity greater than or equals to DEFAULT_CAPACITY
        defaultRestaurantShouldBeFound("capacity.greaterOrEqualThan=" + DEFAULT_CAPACITY);

        // Get all the restaurantList where capacity greater than or equals to UPDATED_CAPACITY
        defaultRestaurantShouldNotBeFound("capacity.greaterOrEqualThan=" + UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    public void getAllRestaurantsByCapacityIsLessThanSomething() throws Exception {
        // Initialize the database
        restaurantRepository.saveAndFlush(restaurant);

        // Get all the restaurantList where capacity less than or equals to DEFAULT_CAPACITY
        defaultRestaurantShouldNotBeFound("capacity.lessThan=" + DEFAULT_CAPACITY);

        // Get all the restaurantList where capacity less than or equals to UPDATED_CAPACITY
        defaultRestaurantShouldBeFound("capacity.lessThan=" + UPDATED_CAPACITY);
    }


    @Test
    @Transactional
    public void getAllRestaurantsByMenuIsEqualToSomething() throws Exception {
        // Initialize the database
        Menu menu = MenuResourceIntTest.createEntity(em);
        em.persist(menu);
        em.flush();
        restaurant.addMenu(menu);
        restaurantRepository.saveAndFlush(restaurant);
        Long menuId = menu.getId();

        // Get all the restaurantList where menu equals to menuId
        defaultRestaurantShouldBeFound("menuId.equals=" + menuId);

        // Get all the restaurantList where menu equals to menuId + 1
        defaultRestaurantShouldNotBeFound("menuId.equals=" + (menuId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultRestaurantShouldBeFound(String filter) throws Exception {
        restRestaurantMockMvc.perform(get("/api/restaurants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(restaurant.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION.toString())))
            .andExpect(jsonPath("$.[*].capacity").value(hasItem(DEFAULT_CAPACITY)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultRestaurantShouldNotBeFound(String filter) throws Exception {
        restRestaurantMockMvc.perform(get("/api/restaurants?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingRestaurant() throws Exception {
        // Get the restaurant
        restRestaurantMockMvc.perform(get("/api/restaurants/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRestaurant() throws Exception {
        // Initialize the database
        restaurantService.save(restaurant);

        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Update the restaurant
        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant.getId()).get();
        // Disconnect from session so that the updates on updatedRestaurant are not directly saved in db
        em.detach(updatedRestaurant);
        updatedRestaurant
            .name(UPDATED_NAME)
            .location(UPDATED_LOCATION)
            .capacity(UPDATED_CAPACITY);

        restRestaurantMockMvc.perform(put("/api/restaurants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedRestaurant)))
            .andExpect(status().isOk());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
        Restaurant testRestaurant = restaurantList.get(restaurantList.size() - 1);
        assertThat(testRestaurant.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRestaurant.getLocation()).isEqualTo(UPDATED_LOCATION);
        assertThat(testRestaurant.getCapacity()).isEqualTo(UPDATED_CAPACITY);
    }

    @Test
    @Transactional
    public void updateNonExistingRestaurant() throws Exception {
        int databaseSizeBeforeUpdate = restaurantRepository.findAll().size();

        // Create the Restaurant

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRestaurantMockMvc.perform(put("/api/restaurants")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(restaurant)))
            .andExpect(status().isBadRequest());

        // Validate the Restaurant in the database
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRestaurant() throws Exception {
        // Initialize the database
        restaurantService.save(restaurant);

        int databaseSizeBeforeDelete = restaurantRepository.findAll().size();

        // Get the restaurant
        restRestaurantMockMvc.perform(delete("/api/restaurants/{id}", restaurant.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Restaurant> restaurantList = restaurantRepository.findAll();
        assertThat(restaurantList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Restaurant.class);
        Restaurant restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        Restaurant restaurant2 = new Restaurant();
        restaurant2.setId(restaurant1.getId());
        assertThat(restaurant1).isEqualTo(restaurant2);
        restaurant2.setId(2L);
        assertThat(restaurant1).isNotEqualTo(restaurant2);
        restaurant1.setId(null);
        assertThat(restaurant1).isNotEqualTo(restaurant2);
    }
}
