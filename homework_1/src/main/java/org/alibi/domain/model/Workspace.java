package org.alibi.domain.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Класс, представляющий рабочее место.
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {
    /**
     * Уникальный идентификатор рабочего места.
     */
    Long id;

    /**
     * Название рабочего места.
     */
    String name;

    /**
     * Доступность рабочего места.
     */
    boolean available;
}
