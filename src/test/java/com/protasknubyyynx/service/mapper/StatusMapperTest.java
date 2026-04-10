package com.protasknubyyynx.service.mapper;

import static com.protasknubyyynx.domain.StatusAsserts.*;
import static com.protasknubyyynx.domain.StatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class StatusMapperTest {

    private StatusMapper statusMapper;

    @BeforeEach
    void setUp() {
        statusMapper = new StatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getStatusSample1();
        var actual = statusMapper.toEntity(statusMapper.toDto(expected));
        assertStatusAllPropertiesEquals(expected, actual);
    }
}
