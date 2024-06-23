package org.alibi.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Класс, представляющий конференц-зал.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ConferenceRoom {
    /**
     * Уникальный идентификатор конференц-зала.
     */
    Long id;

    /**
     * Название конференц-зала.
     */
    String name;

    /**
     * Доступность конференц-зала.
     */
    boolean available;
}
