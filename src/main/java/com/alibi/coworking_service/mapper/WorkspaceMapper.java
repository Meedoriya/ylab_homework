package com.alibi.coworking_service.mapper;

import com.alibi.coworking_service.domain.dto.WorkspaceDto;
import com.alibi.coworking_service.domain.model.Workspace;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WorkspaceMapper {

    WorkspaceDto toDto(Workspace workspace);

    Workspace toEntity(WorkspaceDto workspaceDto);
}
