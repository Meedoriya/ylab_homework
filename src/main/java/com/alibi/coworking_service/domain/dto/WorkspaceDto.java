package com.alibi.coworking_service.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


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
