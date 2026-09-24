package com.taskflow.exception;

/**
 * Кастомное исключение для случая, когда задача не найдена.
 * Наследуется от RuntimeException, чтобы не требовать обязательной обработки (throws).
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }
}