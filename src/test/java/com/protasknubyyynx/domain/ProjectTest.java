package com.protasknubyyynx.domain;

import static com.protasknubyyynx.domain.ProjectTestSamples.*;
import static com.protasknubyyynx.domain.StatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.protasknubyyynx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProjectTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Project.class);
        Project project1 = getProjectSample1();
        Project project2 = new Project();
        assertThat(project1).isNotEqualTo(project2);

        project2.setId(project1.getId());
        assertThat(project1).isEqualTo(project2);

        project2 = getProjectSample2();
        assertThat(project1).isNotEqualTo(project2);
    }

    @Test
    void statusTest() {
        Project project = getProjectRandomSampleGenerator();
        Status statusBack = getStatusRandomSampleGenerator();

        project.setStatus(statusBack);
        assertThat(project.getStatus()).isEqualTo(statusBack);

        project.status(null);
        assertThat(project.getStatus()).isNull();
    }
}
