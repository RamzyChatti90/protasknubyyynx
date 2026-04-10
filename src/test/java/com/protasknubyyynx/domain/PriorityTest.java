package com.protasknubyyynx.domain;

import static com.protasknubyyynx.domain.PriorityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.protasknubyyynx.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Priority.class);
        Priority priority1 = getPrioritySample1();
        Priority priority2 = new Priority();
        assertThat(priority1).isNotEqualTo(priority2);

        priority2.setId(priority1.getId());
        assertThat(priority1).isEqualTo(priority2);

        priority2 = getPrioritySample2();
        assertThat(priority1).isNotEqualTo(priority2);
    }
}
