package com.protasknubyyynx.service.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

/**
 * A DTO for the dashboard data.
 */
public class DashboardDataDTO implements Serializable {

    private Long totalTasks;
    private Long openTasks;
    private Long completedTasks;
    private Map<String, Long> tasksByStatus;
    private Map<String, Long> tasksByPriority;

    // Getters and Setters
    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getOpenTasks() {
        return openTasks;
    }

    public void setOpenTasks(Long openTasks) {
        this.openTasks = openTasks;
    }

    public Long getCompletedTasks() {
        return completedTasks;
    }

    public void setCompletedTasks(Long completedTasks) {
        this.completedTasks = completedTasks;
    }

    public Map<String, Long> getTasksByStatus() {
        return tasksByStatus;
    }

    public void setTasksByStatus(Map<String, Long> tasksByStatus) {
        this.tasksByStatus = tasksByStatus;
    }

    public Map<String, Long> getTasksByPriority() {
        return tasksByPriority;
    }

    public void setTasksByPriority(Map<String, Long> tasksByPriority) {
        this.tasksByPriority = tasksByPriority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardDataDTO that = (DashboardDataDTO) o;
        return (
            Objects.equals(totalTasks, that.totalTasks) &&
            Objects.equals(openTasks, that.openTasks) &&
            Objects.equals(completedTasks, that.completedTasks) &&
            Objects.equals(tasksByStatus, that.tasksByStatus) &&
            Objects.equals(tasksByPriority, that.tasksByPriority)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalTasks, openTasks, completedTasks, tasksByStatus, tasksByPriority);
    }

    @Override
    public String toString() {
        return (
            "DashboardDataDTO{" +
            "totalTasks=" +
            totalTasks +
            ", openTasks=" +
            openTasks +
            ", completedTasks=" +
            completedTasks +
            ", tasksByStatus=" +
            tasksByStatus +
            ", tasksByPriority=" +
            tasksByPriority +
            '}'
        );
    }
}
package com.protasknubyyynx.repository;

import com.protasknubyyynx.domain.Task;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Long countByCreatedBy(String createdBy);

    Long countByCreatedByAndStatus_Name(String createdBy, String statusName);

    @Query("SELECT t.status.name, COUNT(t) FROM Task t WHERE t.createdBy = :createdBy GROUP BY t.status.name")
    List<Object[]> countTasksByStatusForUser(@Param("createdBy") String createdBy);

    @Query("SELECT t.priority.name, COUNT(t) FROM Task t WHERE t.createdBy = :createdBy GROUP BY t.priority.name")
    List<Object[]> countTasksByPriorityForUser(@Param("createdBy") String createdBy);
}
package com.protasknubyyynx.service;

import com.protasknubyyynx.service.dto.DashboardDataDTO;
import com.protasknubyyynx.service.dto.TaskDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.protasknubyyynx.domain.Task}.
 */
public interface TaskService {
    /**
     * Save a task.
     *
     * @param taskDTO the entity to save.
     * @return the persisted entity.
     */
    TaskDTO save(TaskDTO taskDTO);

    /**
     * Updates a task.
     *
     * @param taskDTO the entity to update.
     * @return the persisted entity.
     */
    TaskDTO update(TaskDTO taskDTO);

    /**
     * Partially updates a task.
     *
     * @param taskDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskDTO> partialUpdate(TaskDTO taskDTO);

    /**
     * Get all the tasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskDTO> findAll(Pageable pageable);

    /**
     * Get the "id" task.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskDTO> findOne(Long id);

    /**
     * Delete the "id" task.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get dashboard data for the current user.
     *
     * @return the aggregated dashboard data.
     */
    DashboardDataDTO getDashboardData();
}
package com.protasknubyyynx.service.impl;

import com.protasknubyyynx.domain.Task;
import com.protasknubyyynx.repository.TaskRepository;
import com.protasknubyyynx.security.SecurityUtils;
import com.protasknubyyynx.service.TaskService;
import com.protasknubyyynx.service.dto.DashboardDataDTO;
import com.protasknubyyynx.service.dto.TaskDTO;
import com.protasknubyyynx.service.mapper.TaskMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.protasknubyyynx.domain.Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger LOG = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        LOG.debug("Request to save Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDTO update(TaskDTO taskDTO) {
        LOG.debug("Request to update Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public Optional<TaskDTO> partialUpdate(TaskDTO taskDTO) {
        LOG.debug("Request to partially update Task : {}", taskDTO);

        return taskRepository
            .findById(taskDTO.getId())
            .map(existingTask -> {
                taskMapper.partialUpdate(existingTask, taskDTO);

                return existingTask;
            })
            .map(taskRepository::save)
            .map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Tasks");
        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDTO> findOne(Long id) {
        LOG.debug("Request to get Task : {}", id);
        return taskRepository.findById(id).map(taskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDataDTO getDashboardData() {
        LOG.debug("Request to get dashboard data for current user");
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        DashboardDataDTO dashboardData = new DashboardDataDTO();

        dashboardData.setTotalTasks(taskRepository.countByCreatedBy(currentUserLogin));
        dashboardData.setOpenTasks(taskRepository.countByCreatedByAndStatus_Name(currentUserLogin, "Open")); // Assuming 'Open' is the status name for open tasks
        dashboardData.setCompletedTasks(taskRepository.countByCreatedByAndStatus_Name(currentUserLogin, "Done")); // Assuming 'Done' is the status name for completed tasks

        List<Object[]> tasksByStatus = taskRepository.countTasksByStatusForUser(currentUserLogin);
        Map<String, Long> statusCounts = tasksByStatus
            .stream()
            .collect(Collectors.toMap(
                obj -> (String) obj[0],
                obj -> (Long) obj[1]
            ));
        dashboardData.setTasksByStatus(statusCounts);

        List<Object[]> tasksByPriority = taskRepository.countTasksByPriorityForUser(currentUserLogin);
        Map<String, Long> priorityCounts = tasksByPriority
            .stream()
            .collect(Collectors.toMap(
                obj -> (String) obj[0],
                obj -> (Long) obj[1]
            ));
        dashboardData.setTasksByPriority(priorityCounts);

        return dashboardData;
    }
}
package com.protasknubyyynx.web.rest;

import com.protasknubyyynx.repository.TaskRepository;
import com.protasknubyyynx.service.TaskService;
import com.protasknubyyynx.service.dto.DashboardDataDTO;
import com.protasknubyyynx.service.dto.TaskDTO;
import com.protasknubyyynx.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.protasknubyyynx.domain.Task}.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskResource {

    private static final Logger LOG = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "task";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaskService taskService;

    private final TaskRepository taskRepository;

    public TaskResource(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    /**
     * {@code POST  /tasks} : Create a new task.
     *
     * @param taskDTO the taskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taskDTO, or with status {@code 400 (Bad Request)} if the task has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO) throws URISyntaxException {
        LOG.debug("REST request to save Task : {}", taskDTO);
        if (taskDTO.getId() != null) {
            throw new BadRequestAlertException("A new task cannot already have an ID", ENTITY_NAME, "idexists");
        }
        taskDTO = taskService.save(taskDTO);
        return ResponseEntity.created(new URI("/api/tasks/" + taskDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, taskDTO.getId().toString()))
            .body(taskDTO);
    }

    /**
     * {@code PUT  /tasks/:id} : Updates an existing task.
     *
     * @param id the id of the taskDTO to save.
     * @param taskDTO the taskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskDTO,
     * or with status {@code 400 (Bad Request)} if the taskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskDTO> updateTask(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TaskDTO taskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Task : {}, {}", id, taskDTO);
        if (taskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        taskDTO = taskService.update(taskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskDTO.getId().toString()))
            .body(taskDTO);
    }

    /**
     * {@code PATCH  /tasks/:id} : Partial updates given fields of an existing task, field will ignore if it is null
     *
     * @param id the id of the taskDTO to save.
     * @param taskDTO the taskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taskDTO,
     * or with status {@code 400 (Bad Request)} if the taskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaskDTO> partialUpdateTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TaskDTO taskDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Task partially : {}, {}", id, taskDTO);
        if (taskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaskDTO> result = taskService.partialUpdate(taskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, taskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TaskDTO>> getAllTasks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Tasks");
        Page<TaskDTO> page = taskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" task.
     *
     * @param id the id of the taskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Task : {}", id);
        Optional<TaskDTO> taskDTO = taskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taskDTO);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" task.
     *
     * @param id the id of the taskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Task : {}", id);
        taskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code GET  /tasks/dashboard} : get the dashboard data for the current user.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dashboardDataDTO.
     */
    @GetMapping("/dashboard")
    public ResponseEntity<DashboardDataDTO> getDashboardData() {
        LOG.debug("REST request to get Dashboard Data");
        DashboardDataDTO dashboardData = taskService.getDashboardData();
        return ResponseEntity.ok().body(dashboardData);
    }
}