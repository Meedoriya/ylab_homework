package com.alibi.coworking_service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;



/**
 * DTO для представления конференц-зала.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ConferenceRoomDto {
    Long id;

    @NotBlank(message = "Название обязательно")
    @Size(min = 3, max = 50)
    String name;

    boolean available;
}
