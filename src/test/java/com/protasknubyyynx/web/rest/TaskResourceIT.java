package com.protasknubyyynx.web.rest;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.protasknubyyynx.IntegrationTest;
import com.protasknubyyynx.domain.Priority;
import com.protasknubyyynx.domain.Status;
import com.protasknubyyynx.domain.Task;
import com.protasknubyyynx.repository.TaskRepository;
import com.protasknubyyynx.security.AuthoritiesConstants;
import com.protasknubyyynx.service.dto.DashboardDataDTO;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser", authorities = { AuthoritiesConstants.USER }) // Default mock user for tests
class TaskResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Status DEFAULT_STATUS = Status.TODO;
    private static final Priority DEFAULT_PRIORITY = Priority.LOW;
    private static final String DEFAULT_ASSIGNED_TO = "testuser";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTaskMockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Task task;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .dueDate(DEFAULT_DUE_DATE)
            .status(DEFAULT_STATUS)
            .priority(DEFAULT_PRIORITY)
            .assignedTo(DEFAULT_ASSIGNED_TO);
        return task;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
            .title("BBBBBBBBBB")
            .description("BBBBBBBBBB")
            .dueDate(Instant.now())
            .status(Status.IN_PROGRESS)
            .priority(Priority.HIGH)
            .assignedTo("otheruser");
        return task;
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    @WithMockUser(username = DEFAULT_ASSIGNED_TO, authorities = { AuthoritiesConstants.USER })
    void getDashboardDataForCurrentUser() throws Exception {
        // Initialize the database with some tasks for the test user
        taskRepository.deleteAll(); // Clean up existing tasks to ensure predictable test data

        Task todoLowTask = createEntity(em).title("Todo Low 1").status(Status.TODO).priority(Priority.LOW).assignedTo(DEFAULT_ASSIGNED_TO);
        em.persist(todoLowTask);

        Task todoHighTask = createEntity(em).title("Todo High 1").status(Status.TODO).priority(Priority.HIGH).assignedTo(DEFAULT_ASSIGNED_TO);
        em.persist(todoHighTask);

        Task inProgressMediumTask = createEntity(em).title("In Progress Medium 1").status(Status.IN_PROGRESS).priority(Priority.MEDIUM).assignedTo(DEFAULT_ASSIGNED_TO);
        em.persist(inProgressMediumTask);

        Task doneLowTask = createEntity(em).title("Done Low 1").status(Status.DONE).priority(Priority.LOW).assignedTo(DEFAULT_ASSIGNED_TO);
        em.persist(doneLowTask);

        Task doneMediumTask = createEntity(em).title("Done Medium 1").status(Status.DONE).priority(Priority.MEDIUM).assignedTo(DEFAULT_ASSIGNED_TO);
        em.persist(doneMediumTask);

        // A task assigned to another user should not be counted
        Task otherUserTask = createEntity(em).title("Other User Task").status(Status.TODO).priority(Priority.LOW).assignedTo("otheruser");
        em.persist(otherUserTask);

        em.flush(); // Ensure entities are persisted before counting

        // Perform the GET request
        restTaskMockMvc
            .perform(get("/api/tasks/dashboard-data").with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.statusCounts.TODO").value(2))
            .andExpect(jsonPath("$.statusCounts.IN_PROGRESS").value(1))
            .andExpect(jsonPath("$.statusCounts.DONE").value(2))
            .andExpect(jsonPath("$.statusCounts.BLOCKED").value(0)) // Assuming no BLOCKED tasks
            .andExpect(jsonPath("$.priorityCounts.LOW").value(2))
            .andExpect(jsonPath("$.priorityCounts.MEDIUM").value(2))
            .andExpect(jsonPath("$.priorityCounts.HIGH").value(1))
            .andExpect(jsonPath("$.priorityCounts.URGENT").value(0)); // Assuming no URGENT tasks
    }

    @Test
    @Transactional
    @WithMockUser(username = "nonexistentuser", authorities = { AuthoritiesConstants.USER })
    void getDashboardDataForNonExistentUser() throws Exception {
        // No tasks for "nonexistentuser" should result in all counts being 0
        taskRepository.deleteAll(); // Ensure a clean slate

        restTaskMockMvc
            .perform(get("/api/tasks/dashboard-data").with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.statusCounts.TODO").value(0))
            .andExpect(jsonPath("$.statusCounts.IN_PROGRESS").value(0))
            .andExpect(jsonPath("$.statusCounts.DONE").value(0))
            .andExpect(jsonPath("$.statusCounts.BLOCKED").value(0))
            .andExpect(jsonPath("$.priorityCounts.LOW").value(0))
            .andExpect(jsonPath("$.priorityCounts.MEDIUM").value(0))
            .andExpect(jsonPath("$.priorityCounts.HIGH").value(0))
            .andExpect(jsonPath("$.priorityCounts.URGENT").value(0));
    }

    @Test
    @Transactional
    void getDashboardDataWithoutAuthentication() throws Exception {
        // Should return 401 Unauthorized if not authenticated
        restTaskMockMvc
            .perform(get("/api/tasks/dashboard-data").with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }
}
