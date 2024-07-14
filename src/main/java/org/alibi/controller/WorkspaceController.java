package org.alibi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.alibi.domain.dto.WorkspaceDto;
import org.alibi.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для управления рабочими местами.
 */
@RestController
@RequestMapping("/api/workspaces")
@RequiredArgsConstructor
@Api(tags = "Workspace Controller", description = "API for managing workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * Добавляет новое рабочее место.
     *
     * @param workspaceDto DTO рабочего места
     * @return ResponseEntity с статусом 201
     */
    @PostMapping
    @ApiOperation(value = "Add a new workspace", notes = "Adds a new workspace")
    public ResponseEntity<Void> addWorkspace(@RequestBody WorkspaceDto workspaceDto) {
        workspaceService.addWorkspace(workspaceDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Обновляет существующее рабочее место.
     *
     * @param workspaceDto DTO рабочего места
     * @return ResponseEntity с статусом 200
     */
    @PutMapping
    @ApiOperation(value = "Update a workspace", notes = "Updates an existing workspace")
    public ResponseEntity<Void> updateWorkspace(@RequestBody WorkspaceDto workspaceDto) {
        workspaceService.updateWorkspace(workspaceDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет рабочее место по его ID.
     *
     * @param workspaceId ID рабочего места
     * @return ResponseEntity с статусом 204
     */
    @DeleteMapping("/{workspaceId}")
    @ApiOperation(value = "Delete a workspace", notes = "Deletes a workspace by its ID")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Возвращает список всех рабочих мест.
     *
     * @return ResponseEntity со списком всех рабочих мест
     */
    @GetMapping
    @ApiOperation(value = "Get all workspaces", notes = "Returns a list of all workspaces")
    public ResponseEntity<List<WorkspaceDto>> getAllWorkspaces() {
        List<WorkspaceDto> workspaces = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok(workspaces);
    }
}
