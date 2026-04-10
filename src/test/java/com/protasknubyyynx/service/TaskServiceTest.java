package com.protasknubyyynx.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.protasknubyyynx.domain.Priority;
import com.protasknubyyynx.domain.Status;
import com.protasknubyyynx.domain.Task;
import com.protasknubyyynx.repository.TaskRepository;
import com.protasknubyyynx.security.SecurityUtils;
import com.protasknubyyynx.service.dto.DashboardDataDTO;
import com.protasknubyyynx.service.dto.TaskDTO;
import com.protasknubyyynx.service.impl.TaskServiceImpl;
import com.protasknubyyynx.service.mapper.TaskMapper;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task1;
    private Task task2;
    private TaskDTO taskDTO1;

    @BeforeEach
    void setUp() {
        Status statusTodo = new Status().id(1L).name("To Do");
        Status statusDone = new Status().id(2L).name("Done");
        Priority priorityHigh = new Priority().id(1L).name("High");
        Priority priorityLow = new Priority().id(2L).name("Low");

        task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task 1");
        task1.setCompleted(false);
        task1.setStatus(statusTodo);
        task1.setPriority(priorityHigh);
        task1.setCreatedBy("testuser");

        task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task 2");
        task2.setCompleted(true);
        task2.setStatus(statusDone);
        task2.setPriority(priorityLow);
        task2.setCreatedBy("testuser");

        taskDTO1 = new TaskDTO();
        taskDTO1.setId(1L);
        taskDTO1.setTitle("Task 1");
        taskDTO1.setCompleted(false);
        taskDTO1.setStatusName("To Do");
        taskDTO1.setPriorityName("High");
    }

    @Test
    void save() {
        when(taskMapper.toEntity(taskDTO1)).thenReturn(task1);
        when(taskRepository.save(task1)).thenReturn(task1);
        when(taskMapper.toDto(task1)).thenReturn(taskDTO1);

        TaskDTO result = taskService.save(taskDTO1);

        assertThat(result).isEqualTo(taskDTO1);
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void update() {
        when(taskMapper.toEntity(taskDTO1)).thenReturn(task1);
        when(taskRepository.save(task1)).thenReturn(task1);
        when(taskMapper.toDto(task1)).thenReturn(taskDTO1);

        TaskDTO result = taskService.update(taskDTO1);

        assertThat(result).isEqualTo(taskDTO1);
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void partialUpdate() {
        when(taskRepository.findById(taskDTO1.getId())).thenReturn(Optional.of(task1));
        doNothing().when(taskMapper).partialUpdate(any(Task.class), any(TaskDTO.class));
        when(taskRepository.save(task1)).thenReturn(task1);
        when(taskMapper.toDto(task1)).thenReturn(taskDTO1);

        Optional<TaskDTO> result = taskService.partialUpdate(taskDTO1);

        assertThat(result).isPresent().contains(taskDTO1);
        verify(taskRepository, times(1)).findById(taskDTO1.getId());
        verify(taskMapper, times(1)).partialUpdate(task1, taskDTO1);
        verify(taskRepository, times(1)).save(task1);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Task> tasks = Arrays.asList(task1, task2);
        Page<Task> taskPage = new PageImpl<>(tasks, pageable, tasks.size());

        when(taskRepository.findAll(pageable)).thenReturn(taskPage);
        when(taskMapper.toDto(task1)).thenReturn(taskDTO1);
        when(taskMapper.toDto(task2)).thenReturn(taskDTO1); // Simplified for test, in real scenario would be taskDTO2

        Page<TaskDTO> result = taskService.findAll(pageable);

        assertThat(result.getContent()).hasSize(2);
        verify(taskRepository, times(1)).findAll(pageable);
    }

    @Test
    void findOne() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task1));
        when(taskMapper.toDto(task1)).thenReturn(taskDTO1);

        Optional<TaskDTO> result = taskService.findOne(1L);

        assertThat(result).isPresent().contains(taskDTO1);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void delete() {
        doNothing().when(taskRepository).deleteById(1L);

        taskService.delete(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void getDashboardDataForCurrentUser() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of("testuser"));

            when(taskRepository.countByCreatedBy("testuser")).thenReturn(2L);
            when(taskRepository.countByCreatedByAndCompleted("testuser", true)).thenReturn(1L);
            when(taskRepository.countByCreatedByAndCompleted("testuser", false)).thenReturn(1L);
            when(taskRepository.countTasksByStatusNameAndCreatedBy("testuser"))
                .thenReturn(Arrays.asList(new Object[] { "To Do", 1L }, new Object[] { "Done", 1L }));
            when(taskRepository.countTasksByPriorityNameAndCreatedBy("testuser"))
                .thenReturn(Arrays.asList(new Object[] { "High", 1L }, new Object[] { "Low", 1L }));

            DashboardDataDTO result = taskService.getDashboardDataForCurrentUser();

            assertThat(result.getTotalTasks()).isEqualTo(2L);
            assertThat(result.getCompletedTasks()).isEqualTo(1L);
            assertThat(result.getOpenTasks()).isEqualTo(1L);
            assertThat(result.getTasksByStatus()).hasSize(2).containsEntry("To Do", 1L).containsEntry("Done", 1L);
            assertThat(result.getTasksByPriority()).hasSize(2).containsEntry("High", 1L).containsEntry("Low", 1L);

            verify(taskRepository, times(1)).countByCreatedBy("testuser");
            verify(taskRepository, times(1)).countByCreatedByAndCompleted("testuser", true);
            verify(taskRepository, times(1)).countByCreatedByAndCompleted("testuser", false);
            verify(taskRepository, times(1)).countTasksByStatusNameAndCreatedBy("testuser");
            verify(taskRepository, times(1)).countTasksByPriorityNameAndCreatedBy("testuser");
        }
    }

    @Test
    void getDashboardDataForCurrentUser_noUser() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.empty());

            // Expect an exception when no user is logged in
            org.junit.jupiter.api.Assertions.assertThrows(
                IllegalStateException.class,
                () -> taskService.getDashboardDataForCurrentUser()
            );
        }
    }

    @Test
    void getDashboardDataForCurrentUser_noTasks() {
        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::getCurrentUserLogin).thenReturn(Optional.of("testuser"));

            when(taskRepository.countByCreatedBy("testuser")).thenReturn(0L);
            when(taskRepository.countByCreatedByAndCompleted("testuser", true)).thenReturn(0L);
            when(taskRepository.countByCreatedByAndCompleted("testuser", false)).thenReturn(0L);
            when(taskRepository.countTasksByStatusNameAndCreatedBy("testuser")).thenReturn(Collections.emptyList());
            when(taskRepository.countTasksByPriorityNameAndCreatedBy("testuser")).thenReturn(Collections.emptyList());

            DashboardDataDTO result = taskService.getDashboardDataForCurrentUser();

            assertThat(result.getTotalTasks()).isEqualTo(0L);
            assertThat(result.getCompletedTasks()).isEqualTo(0L);
            assertThat(result.getOpenTasks()).isEqualTo(0L);
            assertThat(result.getTasksByStatus()).isEmpty();
            assertThat(result.getTasksByPriority()).isEmpty();
        }
    }
}
