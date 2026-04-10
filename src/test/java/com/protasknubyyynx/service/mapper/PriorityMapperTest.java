package com.protasknubyyynx.service.mapper;

import static com.protasknubyyynx.domain.PriorityAsserts.*;
import static com.protasknubyyynx.domain.PriorityTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriorityMapperTest {

    private PriorityMapper priorityMapper;

    @BeforeEach
    void setUp() {
        priorityMapper = new PriorityMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPrioritySample1();
        var actual = priorityMapper.toEntity(priorityMapper.toDto(expected));
        assertPriorityAllPropertiesEquals(expected, actual);
    }
}
