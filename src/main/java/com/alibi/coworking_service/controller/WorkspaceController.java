package com.alibi.coworking_service.controller;

import com.alibi.coworking_service.domain.dto.WorkspaceDto;
import com.alibi.coworking_service.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
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
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * Добавляет новое рабочее место.
     *
     * @param workspaceDto DTO рабочего места
     * @return ResponseEntity с статусом 201
     */
    @PostMapping
    @Operation(summary = "Add a new workspace", description = "Adds a new workspace")
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
    @Operation(summary = "Update a workspace", description = "Updates an existing workspace")
    public ResponseEntity<WorkspaceDto> updateWorkspace(@PathVariable Long workspaceId, @RequestBody WorkspaceDto workspaceDto) {
        WorkspaceDto updatedWorkspace = workspaceService.updateWorkspace(workspaceId, workspaceDto);

        return ResponseEntity.ok(updatedWorkspace);
    }

    /**
     * Удаляет рабочее место по его ID.
     *
     * @param workspaceId ID рабочего места
     * @return ResponseEntity с статусом 204
     */
    @DeleteMapping("/{workspaceId}")
    @Operation(summary = "Delete a workspace", description = "Deletes a workspace by its ID")
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
    @Operation(summary = "Get all workspaces", description = "Returns a list of all workspaces")
    public ResponseEntity<List<WorkspaceDto>> getAllWorkspaces() {
        List<WorkspaceDto> workspaces = workspaceService.getAllWorkspaces();
        return ResponseEntity.ok(workspaces);
    }
}