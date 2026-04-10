package com.protasknubyyynx.service.mapper;

import com.protasknubyyynx.domain.Status;
import com.protasknubyyynx.service.dto.StatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Status} and its DTO {@link StatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface StatusMapper extends EntityMapper<StatusDTO, Status> {}
