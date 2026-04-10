package com.protasknubyyynx.repository;

import com.protasknubyyynx.domain.Task;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    // Methods for dashboard data
    @Query("SELECT t.status.name, COUNT(t) FROM Task t WHERE t.createdBy = :login GROUP BY t.status.name")
    List<Object[]> countTasksByStatusForUser(@Param("login") String login);

    @Query("SELECT t.priority.name, COUNT(t) FROM Task t WHERE t.createdBy = :login GROUP BY t.priority.name")
    List<Object[]> countTasksByPriorityForUser(@Param("login") String login);

    // Methods for user-specific data access (as hinted by "Les requêtes filtrent par `createdBy`")
    Page<Task> findAllByCreatedBy(String createdBy, Pageable pageable);

    @Query(
        value = "select distinct task from Task task left join fetch task.priority left join fetch task.status left join fetch task.project where task.createdBy = :login",
        countQuery = "select count(distinct task) from Task task where task.createdBy = :login"
    )
    Page<Task> findAllWithEagerRelationships(@Param("login") String login, Pageable pageable);

    Optional<Task> findByIdAndCreatedBy(Long id, String createdBy);

    @Modifying
    @Query("delete from Task t where t.id = :id and t.createdBy = :createdBy")
    void deleteByIdAndCreatedBy(@Param("id") Long id, @Param("createdBy") String createdBy);
}
package com.protasknubyyynx.service;

import com.protasknubyyynx.service.dto.DashboardDataDTO;
import com.protasknubyyynx.service.dto.TaskDTO;
import java.util.List;
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
     * Get all the tasks for the current user.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskDTO> findAll(Pageable pageable);

    /**
     * Get all the tasks with eager load of many-to-many relationships for the current user.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" task for the current user.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskDTO> findOne(Long id);

    /**
     * Delete the "id" task for the current user.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get dashboard data for the current user.
     *
     * @return the DashboardDataDTO containing task counts by status and priority.
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
 * Service Implementation for managing {@link Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public TaskDTO save(TaskDTO taskDTO) {
        log.debug("Request to save Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public TaskDTO update(TaskDTO taskDTO) {
        log.debug("Request to update Task : {}", taskDTO);
        Task task = taskMapper.toEntity(taskDTO);
        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    @Override
    public Optional<TaskDTO> partialUpdate(TaskDTO taskDTO) {
        log.debug("Request to partially update Task : {}", taskDTO);

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
        log.debug("Request to get all Tasks");
        return SecurityUtils
            .getCurrentUserLogin()
            .map(login -> taskRepository.findAllByCreatedBy(login, pageable))
            .orElseGet(Page::empty)
            .map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TaskDTO> findAllWithEagerRelationships(Pageable pageable) {
        log.debug("Request to get all Tasks with eager relationships");
        return SecurityUtils
            .getCurrentUserLogin()
            .map(login -> taskRepository.findAllWithEagerRelationships(login, pageable))
            .orElseGet(Page::empty)
            .map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDTO> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return SecurityUtils.getCurrentUserLogin().flatMap(login -> taskRepository.findByIdAndCreatedBy(id, login)).map(taskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        SecurityUtils.getCurrentUserLogin().ifPresent(login -> taskRepository.deleteByIdAndCreatedBy(id, login));
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDataDTO getDashboardData() {
        log.debug("Request to get dashboard data for current user");
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Current user login not found"));

        // Get tasks by status
        List<Object[]> tasksByStatusList = taskRepository.countTasksByStatusForUser(currentUserLogin);
        Map<String, Long> tasksByStatusMap = tasksByStatusList
            .stream()
            .collect(Collectors.toMap(
                array -> (String) array[0], // Status name
                array -> (Long) array[1] // Count
            ));

        // Get tasks by priority
        List<Object[]> tasksByPriorityList = taskRepository.countTasksByPriorityForUser(currentUserLogin);
        Map<String, Long> tasksByPriorityMap = tasksByPriorityList
            .stream()
            .collect(Collectors.toMap(
                array -> (String) array[0], // Priority name
                array -> (Long) array[1] // Count
            ));

        DashboardDataDTO dashboardData = new DashboardDataDTO();
        dashboardData.setTasksByStatus(tasksByStatusMap);
        dashboardData.setTasksByPriority(tasksByPriorityMap);

        return dashboardData;
    }
}