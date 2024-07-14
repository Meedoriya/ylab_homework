package org.alibi.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * DTO для представления рабочего пространства.
 */
@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class WorkspaceDto {
        @NotNull
        Long id;

        @NotBlank(message = "Название обязательно")
        String name;

        @NotNull(message = "Статус доступности обязателен")
        Boolean available;
}
