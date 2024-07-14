package org.alibi.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
