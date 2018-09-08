package mk.ukim.finki.career.web.rest;

import mk.ukim.finki.career.CareerApp;

import mk.ukim.finki.career.domain.Post;
import mk.ukim.finki.career.repository.PostRepository;
import mk.ukim.finki.career.service.PostService;
import mk.ukim.finki.career.web.rest.errors.ExceptionTranslator;
import mk.ukim.finki.career.service.dto.PostCriteria;
import mk.ukim.finki.career.service.PostQueryService;

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


import static mk.ukim.finki.career.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import mk.ukim.finki.career.domain.enumeration.JobType;
/**
 * Test class for the PostResource REST controller.
 *
 * @see PostResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CareerApp.class)
public class PostResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_CREATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_CREATION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final JobType DEFAULT_JOB_TYPE = JobType.FULL_TIME_JOB;
    private static final JobType UPDATED_JOB_TYPE = JobType.HALF_TIME_JOB;

    private static final String DEFAULT_CAREER_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CAREER_TYPE = "BBBBBBBBBB";

    @Autowired
    private PostRepository postRepository;
    
    @Autowired
    private PostService postService;

    @Autowired
    private PostQueryService postQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPostMockMvc;

    private Post post;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PostResource postResource = new PostResource(postService, postQueryService);
        this.restPostMockMvc = MockMvcBuilders.standaloneSetup(postResource)
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
    public static Post createEntity(EntityManager em) {
        Post post = new Post()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .creationDate(DEFAULT_CREATION_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .jobType(DEFAULT_JOB_TYPE)
            .careerType(DEFAULT_CAREER_TYPE);
        return post;
    }

    @Before
    public void initTest() {
        post = createEntity(em);
    }

    @Test
    @Transactional
    public void createPost() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post
        restPostMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(post)))
            .andExpect(status().isCreated());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate + 1);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPost.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPost.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testPost.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
        assertThat(testPost.getJobType()).isEqualTo(DEFAULT_JOB_TYPE);
        assertThat(testPost.getCareerType()).isEqualTo(DEFAULT_CAREER_TYPE);
    }

    @Test
    @Transactional
    public void createPostWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = postRepository.findAll().size();

        // Create the Post with an existing ID
        post.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPostMockMvc.perform(post("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(post)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPosts() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList
        restPostMockMvc.perform(get("/api/posts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].careerType").value(hasItem(DEFAULT_CAREER_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getPost() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", post.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(post.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.creationDate").value(DEFAULT_CREATION_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.jobType").value(DEFAULT_JOB_TYPE.toString()))
            .andExpect(jsonPath("$.careerType").value(DEFAULT_CAREER_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title equals to DEFAULT_TITLE
        defaultPostShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the postList where title equals to UPDATED_TITLE
        defaultPostShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultPostShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the postList where title equals to UPDATED_TITLE
        defaultPostShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void getAllPostsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where title is not null
        defaultPostShouldBeFound("title.specified=true");

        // Get all the postList where title is null
        defaultPostShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where description equals to DEFAULT_DESCRIPTION
        defaultPostShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the postList where description equals to UPDATED_DESCRIPTION
        defaultPostShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPostsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultPostShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the postList where description equals to UPDATED_DESCRIPTION
        defaultPostShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllPostsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where description is not null
        defaultPostShouldBeFound("description.specified=true");

        // Get all the postList where description is null
        defaultPostShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByCreationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where creationDate equals to DEFAULT_CREATION_DATE
        defaultPostShouldBeFound("creationDate.equals=" + DEFAULT_CREATION_DATE);

        // Get all the postList where creationDate equals to UPDATED_CREATION_DATE
        defaultPostShouldNotBeFound("creationDate.equals=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByCreationDateIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where creationDate in DEFAULT_CREATION_DATE or UPDATED_CREATION_DATE
        defaultPostShouldBeFound("creationDate.in=" + DEFAULT_CREATION_DATE + "," + UPDATED_CREATION_DATE);

        // Get all the postList where creationDate equals to UPDATED_CREATION_DATE
        defaultPostShouldNotBeFound("creationDate.in=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByCreationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where creationDate is not null
        defaultPostShouldBeFound("creationDate.specified=true");

        // Get all the postList where creationDate is null
        defaultPostShouldNotBeFound("creationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByCreationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where creationDate greater than or equals to DEFAULT_CREATION_DATE
        defaultPostShouldBeFound("creationDate.greaterOrEqualThan=" + DEFAULT_CREATION_DATE);

        // Get all the postList where creationDate greater than or equals to UPDATED_CREATION_DATE
        defaultPostShouldNotBeFound("creationDate.greaterOrEqualThan=" + UPDATED_CREATION_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByCreationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where creationDate less than or equals to DEFAULT_CREATION_DATE
        defaultPostShouldNotBeFound("creationDate.lessThan=" + DEFAULT_CREATION_DATE);

        // Get all the postList where creationDate less than or equals to UPDATED_CREATION_DATE
        defaultPostShouldBeFound("creationDate.lessThan=" + UPDATED_CREATION_DATE);
    }


    @Test
    @Transactional
    public void getAllPostsByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where dueDate equals to DEFAULT_DUE_DATE
        defaultPostShouldBeFound("dueDate.equals=" + DEFAULT_DUE_DATE);

        // Get all the postList where dueDate equals to UPDATED_DUE_DATE
        defaultPostShouldNotBeFound("dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where dueDate in DEFAULT_DUE_DATE or UPDATED_DUE_DATE
        defaultPostShouldBeFound("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE);

        // Get all the postList where dueDate equals to UPDATED_DUE_DATE
        defaultPostShouldNotBeFound("dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where dueDate is not null
        defaultPostShouldBeFound("dueDate.specified=true");

        // Get all the postList where dueDate is null
        defaultPostShouldNotBeFound("dueDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByDueDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where dueDate greater than or equals to DEFAULT_DUE_DATE
        defaultPostShouldBeFound("dueDate.greaterOrEqualThan=" + DEFAULT_DUE_DATE);

        // Get all the postList where dueDate greater than or equals to UPDATED_DUE_DATE
        defaultPostShouldNotBeFound("dueDate.greaterOrEqualThan=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void getAllPostsByDueDateIsLessThanSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where dueDate less than or equals to DEFAULT_DUE_DATE
        defaultPostShouldNotBeFound("dueDate.lessThan=" + DEFAULT_DUE_DATE);

        // Get all the postList where dueDate less than or equals to UPDATED_DUE_DATE
        defaultPostShouldBeFound("dueDate.lessThan=" + UPDATED_DUE_DATE);
    }


    @Test
    @Transactional
    public void getAllPostsByJobTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where jobType equals to DEFAULT_JOB_TYPE
        defaultPostShouldBeFound("jobType.equals=" + DEFAULT_JOB_TYPE);

        // Get all the postList where jobType equals to UPDATED_JOB_TYPE
        defaultPostShouldNotBeFound("jobType.equals=" + UPDATED_JOB_TYPE);
    }

    @Test
    @Transactional
    public void getAllPostsByJobTypeIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where jobType in DEFAULT_JOB_TYPE or UPDATED_JOB_TYPE
        defaultPostShouldBeFound("jobType.in=" + DEFAULT_JOB_TYPE + "," + UPDATED_JOB_TYPE);

        // Get all the postList where jobType equals to UPDATED_JOB_TYPE
        defaultPostShouldNotBeFound("jobType.in=" + UPDATED_JOB_TYPE);
    }

    @Test
    @Transactional
    public void getAllPostsByJobTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where jobType is not null
        defaultPostShouldBeFound("jobType.specified=true");

        // Get all the postList where jobType is null
        defaultPostShouldNotBeFound("jobType.specified=false");
    }

    @Test
    @Transactional
    public void getAllPostsByCareerTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where careerType equals to DEFAULT_CAREER_TYPE
        defaultPostShouldBeFound("careerType.equals=" + DEFAULT_CAREER_TYPE);

        // Get all the postList where careerType equals to UPDATED_CAREER_TYPE
        defaultPostShouldNotBeFound("careerType.equals=" + UPDATED_CAREER_TYPE);
    }

    @Test
    @Transactional
    public void getAllPostsByCareerTypeIsInShouldWork() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where careerType in DEFAULT_CAREER_TYPE or UPDATED_CAREER_TYPE
        defaultPostShouldBeFound("careerType.in=" + DEFAULT_CAREER_TYPE + "," + UPDATED_CAREER_TYPE);

        // Get all the postList where careerType equals to UPDATED_CAREER_TYPE
        defaultPostShouldNotBeFound("careerType.in=" + UPDATED_CAREER_TYPE);
    }

    @Test
    @Transactional
    public void getAllPostsByCareerTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        postRepository.saveAndFlush(post);

        // Get all the postList where careerType is not null
        defaultPostShouldBeFound("careerType.specified=true");

        // Get all the postList where careerType is null
        defaultPostShouldNotBeFound("careerType.specified=false");
    }
    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultPostShouldBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(post.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(DEFAULT_CREATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].jobType").value(hasItem(DEFAULT_JOB_TYPE.toString())))
            .andExpect(jsonPath("$.[*].careerType").value(hasItem(DEFAULT_CAREER_TYPE.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultPostShouldNotBeFound(String filter) throws Exception {
        restPostMockMvc.perform(get("/api/posts?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingPost() throws Exception {
        // Get the post
        restPostMockMvc.perform(get("/api/posts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePost() throws Exception {
        // Initialize the database
        postService.save(post);

        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Update the post
        Post updatedPost = postRepository.findById(post.getId()).get();
        // Disconnect from session so that the updates on updatedPost are not directly saved in db
        em.detach(updatedPost);
        updatedPost
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .creationDate(UPDATED_CREATION_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .jobType(UPDATED_JOB_TYPE)
            .careerType(UPDATED_CAREER_TYPE);

        restPostMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPost)))
            .andExpect(status().isOk());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
        Post testPost = postList.get(postList.size() - 1);
        assertThat(testPost.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPost.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPost.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testPost.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
        assertThat(testPost.getJobType()).isEqualTo(UPDATED_JOB_TYPE);
        assertThat(testPost.getCareerType()).isEqualTo(UPDATED_CAREER_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPost() throws Exception {
        int databaseSizeBeforeUpdate = postRepository.findAll().size();

        // Create the Post

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPostMockMvc.perform(put("/api/posts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(post)))
            .andExpect(status().isBadRequest());

        // Validate the Post in the database
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePost() throws Exception {
        // Initialize the database
        postService.save(post);

        int databaseSizeBeforeDelete = postRepository.findAll().size();

        // Get the post
        restPostMockMvc.perform(delete("/api/posts/{id}", post.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Post> postList = postRepository.findAll();
        assertThat(postList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = new Post();
        post1.setId(1L);
        Post post2 = new Post();
        post2.setId(post1.getId());
        assertThat(post1).isEqualTo(post2);
        post2.setId(2L);
        assertThat(post1).isNotEqualTo(post2);
        post1.setId(null);
        assertThat(post1).isNotEqualTo(post2);
    }
}
