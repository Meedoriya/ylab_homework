package org.alibi.mapper;

import org.alibi.domain.model.Workspace;
import org.alibi.dto.WorkspaceDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper для преобразования между Workspace и WorkspaceDto.
 */
@Mapper
public interface WorkspaceMapper {

    WorkspaceMapper INSTANCE = Mappers.getMapper(WorkspaceMapper.class);

    WorkspaceDto toDto(Workspace workspace);

    Workspace toEntity(WorkspaceDto workspaceDto);
}
