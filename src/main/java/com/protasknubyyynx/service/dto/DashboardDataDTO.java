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
