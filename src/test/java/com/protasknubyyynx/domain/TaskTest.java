package com.protasknubyyynx.domain;

import static com.protasknubyyynx.domain.PriorityTestSamples.*;
import static com.protasknubyyynx.domain.ProjectTestSamples.*;
import static com.protasknubyyynx.domain.StatusTestSamples.*;
import static com.protasknubyyynx.domain.TaskTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.protasknubyyynx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Task.class);
        Task task1 = getTaskSample1();
        Task task2 = new Task();
        assertThat(task1).isNotEqualTo(task2);

        task2.setId(task1.getId());
        assertThat(task1).isEqualTo(task2);

        task2 = getTaskSample2();
        assertThat(task1).isNotEqualTo(task2);
    }

    @Test
    void projectTest() {
        Task task = getTaskRandomSampleGenerator();
        Project projectBack = getProjectRandomSampleGenerator();

        task.setProject(projectBack);
        assertThat(task.getProject()).isEqualTo(projectBack);

        task.project(null);
        assertThat(task.getProject()).isNull();
    }

    @Test
    void statusTest() {
        Task task = getTaskRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        task.setStatus(statusBack);
        assertThat(task.getStatus()).isEqualTo(statusBack);

        task.status(null);
        assertThat(task.getStatus()).isNull();
    }

    @Test
    void priorityTest() {
        Task task = getTaskRandomSampleGenerator();
        Priority priorityBack = getPriorityRandomSampleGenerator();

        task.setPriority(priorityBack);
        assertThat(task.getPriority()).isEqualTo(priorityBack);

        task.priority(null);
        assertThat(task.getPriority()).isNull();
    }
}
