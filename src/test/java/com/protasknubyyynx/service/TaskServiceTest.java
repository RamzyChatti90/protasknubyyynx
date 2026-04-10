package com.protasknubyyynx.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.protasknubyyynx.domain.Priority;
import com.protasknubyyynx.domain.Status;
import com.protasknubyyynx.repository.TaskRepository;
import com.protasknubyyynx.security.SecurityUtils;
import com.protasknubyyynx.service.dto.DashboardDataDTO;
import com.protasknubyyynx.service.impl.TaskServiceImpl;
import com.protasknubyyynx.service.mapper.TaskMapper;
import com.protasknubyyynx.web.rest.errors.BadRequestAlertException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private static final String TEST_USER_LOGIN = "testuser";

    @BeforeEach
    void setup() {
        // Initialize taskService with mocks. @InjectMocks does this automatically.
    }

    @Test
    @WithMockUser(username = TEST_USER_LOGIN) // This annotation helps set up security context for tests
    void getDashboardDataForCurrentUserShouldReturnCorrectCounts() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of(TEST_USER_LOGIN));

            // Mock repository calls for status counts
            when(taskRepository.countByAssignedToAndStatus(TEST_USER_LOGIN, Status.TODO)).thenReturn(5L);
            when(taskRepository.countByAssignedToAndStatus(TEST_USER_LOGIN, Status.IN_PROGRESS)).thenReturn(3L);
            when(taskRepository.countByAssignedToAndStatus(TEST_USER_LOGIN, Status.DONE)).thenReturn(10L);
            when(taskRepository.countByAssignedToAndStatus(TEST_USER_LOGIN, Status.BLOCKED)).thenReturn(1L);

            // Mock repository calls for priority counts
            when(taskRepository.countByAssignedToAndPriority(TEST_USER_LOGIN, Priority.LOW)).thenReturn(2L);
            when(taskRepository.countByAssignedToAndPriority(TEST_USER_LOGIN, Priority.MEDIUM)).thenReturn(7L);
            when(taskRepository.countByAssignedToAndPriority(TEST_USER_LOGIN, Priority.HIGH)).thenReturn(9L);
            when(taskRepository.countByAssignedToAndPriority(TEST_USER_LOGIN, Priority.URGENT)).thenReturn(1L);


            DashboardDataDTO dashboardData = taskService.getDashboardDataForCurrentUser();

            assertThat(dashboardData).isNotNull();

            // Verify status counts
            assertThat(dashboardData.getStatusCounts()).hasSize(Status.values().length);
            assertThat(dashboardData.getStatusCounts().get(Status.TODO)).isEqualTo(5L);
            assertThat(dashboardData.getStatusCounts().get(Status.IN_PROGRESS)).isEqualTo(3L);
            assertThat(dashboardData.getStatusCounts().get(Status.DONE)).isEqualTo(10L);
            assertThat(dashboardData.getStatusCounts().get(Status.BLOCKED)).isEqualTo(1L);

            // Verify priority counts
            assertThat(dashboardData.getPriorityCounts()).hasSize(Priority.values().length);
            assertThat(dashboardData.getPriorityCounts().get(Priority.LOW)).isEqualTo(2L);
            assertThat(dashboardData.getPriorityCounts().get(Priority.MEDIUM)).isEqualTo(7L);
            assertThat(dashboardData.getPriorityCounts().get(Priority.HIGH)).isEqualTo(9L);
            assertThat(dashboardData.getPriorityCounts().get(Priority.URGENT)).isEqualTo(1L);

            // Verify that repository methods were called for each enum value
            for (Status status : Status.values()) {
                verify(taskRepository).countByAssignedToAndStatus(TEST_USER_LOGIN, status);
            }
            for (Priority priority : Priority.values()) {
                verify(taskRepository).countByAssignedToAndPriority(TEST_USER_LOGIN, priority);
            }
        }
    }

    @Test
    void getDashboardDataForCurrentUserShouldThrowExceptionIfUserNotLoggedIn() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.empty());

            org.assertj.core.api.Assertions.assertThatThrownBy(() -> taskService.getDashboardDataForCurrentUser())
                .isInstanceOf(BadRequestAlertException.class)
                .hasMessageContaining("Current user login not found");
        }
    }
}
