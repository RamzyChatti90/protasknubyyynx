package com.protasknubyyynx.service.mapper;

import com.protasknubyyynx.domain.Project;
import com.protasknubyyynx.domain.Status;
import com.protasknubyyynx.service.dto.ProjectDTO;
import com.protasknubyyynx.service.dto.StatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Project} and its DTO {@link ProjectDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProjectMapper extends EntityMapper<ProjectDTO, Project> {
    @Mapping(target = "status", source = "status", qualifiedByName = "statusId")
    ProjectDTO toDto(Project s);

    @Named("statusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StatusDTO toDtoStatusId(Status status);
}
