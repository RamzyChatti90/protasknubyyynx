package com.protasknubyyynx.service.mapper;

import com.protasknubyyynx.domain.Priority;
import com.protasknubyyynx.service.dto.PriorityDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Priority} and its DTO {@link PriorityDTO}.
 */
@Mapper(componentModel = "spring")
public interface PriorityMapper extends EntityMapper<PriorityDTO, Priority> {}
