package org.alibi.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.alibi.domain.dto.ConferenceRoomDto;
import org.alibi.service.ConferenceRoomService;
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
@Api(tags = "Conference Room Controller", description = "API for managing conference rooms")
public class ConferenceRoomController {

    private final ConferenceRoomService conferenceRoomService;

    /**
     * Добавляет новый конференц-зал.
     *
     * @param conferenceRoomDto DTO конференц-зала
     * @return ResponseEntity с статусом 201
     */
    @PostMapping
    @ApiOperation(value = "Add a new conference room", notes = "Adds a new conference room")
    public ResponseEntity<Void> addConferenceRoom(@RequestBody ConferenceRoomDto conferenceRoomDto) {
        conferenceRoomService.addConferenceRoom(conferenceRoomDto);
        return ResponseEntity.status(201).build();
    }

    /**
     * Обновляет существующий конференц-зал.
     *
     * @param conferenceRoomDto DTO конференц-зала
     * @return ResponseEntity с статусом 200
     */
    @PutMapping
    @ApiOperation(value = "Update a conference room", notes = "Updates an existing conference room")
    public ResponseEntity<Void> updateConferenceRoom(@RequestBody ConferenceRoomDto conferenceRoomDto) {
        conferenceRoomService.updateConferenceRoom(conferenceRoomDto);
        return ResponseEntity.ok().build();
    }

    /**
     * Удаляет конференц-зал по его ID.
     *
     * @param conferenceRoomId ID конференц-зала
     * @return ResponseEntity с статусом 204
     */
    @DeleteMapping("/{conferenceRoomId}")
    @ApiOperation(value = "Delete a conference room", notes = "Deletes a conference room by its ID")
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
    @ApiOperation(value = "Get all conference rooms", notes = "Returns a list of all conference rooms")
    public ResponseEntity<List<ConferenceRoomDto>> getAllConferenceRooms() {
        List<ConferenceRoomDto> conferenceRooms = conferenceRoomService.getAllConferenceRooms();
        return ResponseEntity.ok(conferenceRooms);
    }
}
