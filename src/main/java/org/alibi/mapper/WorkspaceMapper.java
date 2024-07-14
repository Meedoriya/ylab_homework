package org.alibi.mapper;

import org.alibi.domain.dto.WorkspaceDto;
import org.alibi.domain.model.Workspace;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

/**
 * Mapper для преобразования между Workspace и WorkspaceDto.
 */
@Mapper(componentModel = "spring")
@Component
public interface WorkspaceMapper {

    WorkspaceDto toDto(Workspace workspace);

    Workspace toEntity(WorkspaceDto workspaceDto);
}
