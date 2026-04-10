package com.protasknubyyynx.service.impl;

import com.protasknubyyynx.domain.Priority; // Import Priority
import com.protasknubyyynx.domain.Status;   // Import Status
import com.protasknubyyynx.domain.Task;
import com.protasknubyyynx.repository.TaskRepository;
import com.protasknubyyynx.security.SecurityUtils; // Import SecurityUtils
import com.protasknubyyynx.service.TaskService;
import com.protasknubyyynx.service.dto.TaskDTO;
import com.protasknubyyynx.service.dto.DashboardDataDTO; // Import DashboardDataDTO
import com.protasknubyyynx.service.mapper.TaskMapper;
import java.util.Arrays; // For iterating enums
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors; // For Collectors.toMap
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.protasknubyyynx.web.rest.errors.BadRequestAlertException; // For handling missing user login

/**
 * Service Implementation for managing {@link com.protasknubyyynx.domain.Task}.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private static final Logger log = LoggerFactory.getLogger(TaskServiceImpl.class);

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
        return taskRepository.findAll(pageable).map(taskMapper::toDto);
    }

    public Page<TaskDTO> findAllWithEagerRelationships(Pageable pageable) {
        return taskRepository.findAllWithEagerRelationships(pageable).map(taskMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TaskDTO> findOne(Long id) {
        log.debug("Request to get Task : {}", id);
        return taskRepository.findOneWithEagerRelationships(id).map(taskMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Task : {}", id);
        taskRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardDataDTO getDashboardDataForCurrentUser() {
        String currentUserLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new BadRequestAlertException("Current user login not found", "userManagement", "usernotfound"));

        log.debug("Request to get dashboard data for user: {}", currentUserLogin);

        Map<Status, Long> statusCounts = Arrays
            .stream(Status.values())
            .collect(Collectors.toMap(status -> status, status -> taskRepository.countByAssignedToAndStatus(currentUserLogin, status)));

        Map<Priority, Long> priorityCounts = Arrays
            .stream(Priority.values())
            .collect(
                Collectors.toMap(priority -> priority, priority -> taskRepository.countByAssignedToAndPriority(currentUserLogin, priority))
            );

        return new DashboardDataDTO(statusCounts, priorityCounts);
    }
}
