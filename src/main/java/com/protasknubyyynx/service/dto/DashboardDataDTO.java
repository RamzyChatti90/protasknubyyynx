package com.protasknubyyynx.service.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DashboardDataDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, Long> tasksByStatus;
    private Map<String, Long> tasksByPriority;

    public DashboardDataDTO() {
        this.tasksByStatus = new HashMap<>();
        this.tasksByPriority = new HashMap<>();
    }

    public DashboardDataDTO(Map<String, Long> tasksByStatus, Map<String, Long> tasksByPriority) {
        this.tasksByStatus = tasksByStatus;
        this.tasksByPriority = tasksByPriority;
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
        return Objects.equals(tasksByStatus, that.tasksByStatus) && Objects.equals(tasksByPriority, that.tasksByPriority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tasksByStatus, tasksByPriority);
    }

    @Override
    public String toString() {
        return "DashboardDataDTO{" +
               "tasksByStatus=" + tasksByStatus +
               ", tasksByPriority=" + tasksByPriority +
               '}';
    }
}
