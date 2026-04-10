package com.protasknubyyynx.service.mapper;

import com.protasknubyyynx.domain.Priority;
import com.protasknubyyynx.domain.Project;
import com.protasknubyyynx.domain.Status;
import com.protasknubyyynx.domain.Task;
import com.protasknubyyynx.service.dto.PriorityDTO;
import com.protasknubyyynx.service.dto.ProjectDTO;
import com.protasknubyyynx.service.dto.StatusDTO;
import com.protasknubyyynx.service.dto.TaskDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "project", source = "project", qualifiedByName = "projectId")
    @Mapping(target = "status", source = "status", qualifiedByName = "statusId")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "priorityId")
    TaskDTO toDto(Task s);

    @Named("projectId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProjectDTO toDtoProjectId(Project project);

    @Named("statusId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StatusDTO toDtoStatusId(Status status);

    @Named("priorityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PriorityDTO toDtoPriorityId(Priority priority);
}
