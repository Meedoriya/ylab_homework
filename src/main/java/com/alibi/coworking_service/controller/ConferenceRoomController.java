package com.alibi.coworking_service.controller;


import com.alibi.coworking_service.domain.dto.ConferenceRoomDto;
import com.alibi.coworking_service.service.ConferenceRoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Контроллер для управления конференц-залами.
 */
@RestController
@RequestMapping("/api/conference-rooms")
@RequiredArgsConstructor
public class ConferenceRoomController {

    private final ConferenceRoomService conferenceRoomService;

    @PostMapping
    @Operation(summary = "Add a new conference room", description = "Adds a new conference room")
    public ResponseEntity<Void> addConferenceRoom(@RequestBody ConferenceRoomDto conferenceRoomDto) {
        conferenceRoomService.addConferenceRoom(conferenceRoomDto);
        return ResponseEntity.status(201).build();
    }


    /**
     * Обновляет существующий конференц-зал.
     *
     * @param conferenceRoomDto DTO конференц-зала
     * @param conferenceRoomId конференц-зала
     * @return ResponseEntity с статусом 200 и обновленным DTO конференц-зала
     */
    @PutMapping
    @Operation(summary = "Update a conference room", description = "Updates an existing conference room")
    public ResponseEntity<ConferenceRoomDto> updateConferenceRoom(
            @PathVariable Long conferenceRoomId,
            @RequestBody ConferenceRoomDto conferenceRoomDto) {

        ConferenceRoomDto updatedConferenceRoomDto = conferenceRoomService.updateConferenceRoom(conferenceRoomId, conferenceRoomDto);

        return ResponseEntity.ok(updatedConferenceRoomDto);
    }


    /**
     * Удаляет конференц-зал по его ID.
     *
     * @param conferenceRoomId ID конференц-зала
     * @return ResponseEntity с статусом 204
     */
    @DeleteMapping("/{conferenceRoomId}")
    @Operation(summary = "Delete a conference room", description = "Deletes a conference room by its ID")
    public ResponseEntity<Void> deleteConferenceRoom(@PathVariable Long conferenceRoomId) {
        conferenceRoomService.deleteConferenceRoom(conferenceRoomId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Возвращает список всех конференц-залов.
     *
     * @return ResponseEntity со списком всех конференц-залов
     */
    @GetMapping
    @Operation(summary = "Get all conference rooms", description = "Returns a list of all conference rooms")
    public ResponseEntity<List<ConferenceRoomDto>> getAllConferenceRooms() {
        List<ConferenceRoomDto> conferenceRooms = conferenceRoomService.getAllConferenceRooms();
        return ResponseEntity.ok(conferenceRooms);
    }

    /**
     * Возвращает конференц-зал по его ID
     *
     * @param conferenceRoomId
     * @return ResponseEntity со статусом 200 ok
     */
    @GetMapping("/{conferenceRoomId}")
    @Operation(summary = "Get a conference room by ID", description = "Returns a conference room by its ID")
    public ResponseEntity<ConferenceRoomDto> getConferenceRoomById(@PathVariable Long conferenceRoomId) {
        return conferenceRoomService.getConferenceRoomById(conferenceRoomId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
