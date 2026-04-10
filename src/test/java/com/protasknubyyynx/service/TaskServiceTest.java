package com.protasknubyyynx.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.protasknubyyynx.repository.TaskRepository;
import com.protasknubyyynx.security.SecurityUtils;
import com.protasknubyyynx.service.dto.DashboardDataDTO;
import com.protasknubyyynx.service.impl.TaskServiceImpl;
import com.protasknubyyynx.service.mapper.TaskMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper; // Although not directly used in getDashboardDataForCurrentUser, it's a dependency

    @InjectMocks
    private TaskServiceImpl taskService;

    @BeforeEach
    void setup() {
        // Clear security context before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void getDashboardDataForCurrentUser_shouldReturnCorrectCounts() {
        // Given
        String testLogin = "user";
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of(testLogin));

            List<Object[]> statusCounts = Arrays.asList(new Object[] { "To Do", 2L }, new Object[] { "Done", 1L });
            List<Object[]> priorityCounts = Arrays.asList(new Object[] { "High", 1L }, new Object[] { "Medium", 2L });

            when(taskRepository.countTasksByStatusNameAndCreatedBy(testLogin)).thenReturn(statusCounts);
            when(taskRepository.countTasksByPriorityNameAndCreatedBy(testLogin)).thenReturn(priorityCounts);

            // When
            DashboardDataDTO result = taskService.getDashboardDataForCurrentUser();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTasksByStatus()).hasSize(2).containsEntry("To Do", 2L).containsEntry("Done", 1L);
            assertThat(result.getTasksByPriority()).hasSize(2).containsEntry("High", 1L).containsEntry("Medium", 2L);

            verify(taskRepository).countTasksByStatusNameAndCreatedBy(testLogin);
            verify(taskRepository).countTasksByPriorityNameAndCreatedBy(testLogin);
        }
    }

    @Test
    void getDashboardDataForCurrentUser_shouldHandleNoTasks() {
        // Given
        String testLogin = "user";
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of(testLogin));

            when(taskRepository.countTasksByStatusNameAndCreatedBy(testLogin)).thenReturn(Collections.emptyList());
            when(taskRepository.countTasksByPriorityNameAndCreatedBy(testLogin)).thenReturn(Collections.emptyList());

            // When
            DashboardDataDTO result = taskService.getDashboardDataForCurrentUser();

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getTasksByStatus()).isEmpty();
            assertThat(result.getTasksByPriority()).isEmpty();

            verify(taskRepository).countTasksByStatusNameAndCreatedBy(testLogin);
            verify(taskRepository).countTasksByPriorityNameAndCreatedBy(testLogin);
        }
    }

    @Test
    void getDashboardDataForCurrentUser_shouldThrowExceptionIfUserNotLoggedIn() {
        // Given
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.empty());

            // When / Then
            assertThat(
                () -> taskService.getDashboardDataForCurrentUser()
            )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Current user login not found");

            verifyNoInteractions(taskRepository);
        }
    }
}
