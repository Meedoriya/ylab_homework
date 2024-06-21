package org.alibi.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

/**
 * Класс, представляющий бронирование.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    /**
     * Уникальный идентификатор бронирования.
     */
    Long id;

    /**
     * Идентификатор пользователя, сделавшего бронирование.
     */
    Long userId;

    /**
     * Идентификатор ресурса (рабочего места или конференц-зала).
     */
    Long resourceId; // id Workspace or ConferenceRoom

    /**
     * Время начала бронирования.
     */
    LocalDateTime startTime;

    /**
     * Время окончания бронирования.
     */
    LocalDateTime endTime;
}
