package com.protasknubyyynx.service.dto;

import com.protasknubyyynx.domain.Priority;
import com.protasknubyyynx.domain.Status;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class DashboardDataDTO implements Serializable {

    private Map<Status, Long> statusCounts;
    private Map<Priority, Long> priorityCounts;

    public DashboardDataDTO() {
        // Empty constructor needed for Jackson.
    }

    public DashboardDataDTO(Map<Status, Long> statusCounts, Map<Priority, Long> priorityCounts) {
        this.statusCounts = statusCounts;
        this.priorityCounts = priorityCounts;
    }

    public Map<Status, Long> getStatusCounts() {
        return statusCounts;
    }

    public void setStatusCounts(Map<Status, Long> statusCounts) {
        this.statusCounts = statusCounts;
    }

    public Map<Priority, Long> getPriorityCounts() {
        return priorityCounts;
    }

    public void setPriorityCounts(Map<Priority, Long> priorityCounts) {
        this.priorityCounts = priorityCounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DashboardDataDTO that = (DashboardDataDTO) o;
        return Objects.equals(statusCounts, that.statusCounts) && Objects.equals(priorityCounts, that.priorityCounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statusCounts, priorityCounts);
    }

    @Override
    public String toString() {
        return "DashboardDataDTO{" +
               "statusCounts=" + statusCounts +
               ", priorityCounts=" + priorityCounts +
               '}';
    }
}
package com.protasknubyyynx.repository;

import com.protasknubyyynx.domain.Priority;
import com.protasknubyyynx.domain.Status;
import com.protasknubyyynx.domain.Task;
import java.util.Map;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    /**
     * Counts tasks grouped by their status.
     * Assumes Status is an enum or an entity that can be directly used in GROUP BY and as a map key.
     *
     * @return A map where keys are Status objects and values are the counts of tasks for that status.
     */
    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    Map<Status, Long> countTasksByStatus();

    /**
     * Counts tasks grouped by their priority.
     * Assumes Priority is an enum or an entity that can be directly used in GROUP BY and as a map key.
     *
     * @return A map where keys are Priority objects and values are the counts of tasks for that priority.
     */
    @Query("SELECT t.priority, COUNT(t) FROM Task t GROUP BY t.priority")
    Map<Priority, Long> countTasksByPriority();
}
package com.protasknubyyynx.service;

import com.protasknubyyynx.repository.TaskRepository;
import com.protasknubyyynx.service.dto.DashboardDataDTO;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.protasknubyyynx.domain.Task}.
 */
@Service
@Transactional
public class TaskService {

    private final Logger log = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Get dashboard data including task counts by status and priority.
     *
     * @return the {@link DashboardDataDTO} containing aggregated data.
     */
    @Transactional(readOnly = true)
    public DashboardDataDTO getDashboardData() {
        log.debug("Request to get Dashboard Data");

        Map<com.protasknubyyynx.domain.Status, Long> statusCounts = taskRepository.countTasksByStatus();
        Map<com.protasknubyyynx.domain.Priority, Long> priorityCounts = taskRepository.countTasksByPriority();

        return new DashboardDataDTO(statusCounts, priorityCounts);
    }

    // Other existing TaskService methods (save, update, partialUpdate, findAll, findOne, delete) would typically be here.
    // For brevity and focus on the requested compilation fix, only the getDashboardData method is included.
}