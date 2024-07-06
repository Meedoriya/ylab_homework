package org.alibi.dto;

import java.util.List;

/**
 * DTO для представления ошибок валидации.
 */
public class ValidationErrorResponse {
    private List<ValidationError> errors;

    public ValidationErrorResponse(List<ValidationError> errors) {
        this.errors = errors;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    /**
     * Класс для представления отдельной ошибки валидации.
     */
    public static class ValidationError {
        private String field;
        private String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
