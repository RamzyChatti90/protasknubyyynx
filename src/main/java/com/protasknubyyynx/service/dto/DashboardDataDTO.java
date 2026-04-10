package com.protasknubyyynx.service.dto;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;

/**
 * A DTO for the dashboard data.
 */
public class DashboardDataDTO implements Serializable {

    private Long totalTasks;
    private Long openTasks;
    private Long completedTasks;
    private Map<String, Long> tasksByStatus = new HashMap<>();
    private Map<String, Long> tasksByPriority = new HashMap<>();

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
