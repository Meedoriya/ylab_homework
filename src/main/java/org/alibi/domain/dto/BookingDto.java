package org.alibi.domain.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * DTO для представления бронирования.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {
    @NotNull
    Long id;

    @NotNull(message = "ID пользователя обязателен")
    Long userId;

    @NotNull(message = "ID ресурса обязателен")
    Long resourceId;

    @NotNull(message = "Начальное время обязательно")
    @Future(message = "Начальное время должно быть в будущем")
    LocalDateTime startTime;

    @NotNull(message = "Конечное время обязательно")
    @Future(message = "Конечное время должно быть в будущем")
    LocalDateTime endTime;
}
