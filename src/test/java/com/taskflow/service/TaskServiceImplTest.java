package com.taskflow.service;

import com.taskflow.domain.Priority;
import com.taskflow.domain.Status;
import com.taskflow.domain.Task;
import com.taskflow.dto.TaskCreateRequest;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit-тесты для TaskServiceImpl.
 * Используем Mockito для мокирования зависимости TaskRepository.
 */
@ExtendWith(MockitoExtension.class) // Подключаем Mockito
class TaskServiceImplTest {

    @Mock // Создаем мок (заглушку) для репозитория
    private TaskRepository taskRepository;

    @InjectMocks // Внедряем моки в тестируемый сервис
    private TaskServiceImpl taskService;

    private TaskCreateRequest validRequest;
    private Task sampleTask;

    @BeforeEach
    void setUp() {
        // Готовим тестовые данные
        validRequest = new TaskCreateRequest();
        validRequest.setTitle("Test Task");
        validRequest.setDescription("Test Description");
        validRequest.setPriority(Priority.HIGH);
        validRequest.setDeadline(LocalDateTime.now().plusDays(1));

        sampleTask = Task.createNew(
                "Test Task",
                "Test Description",
                Priority.HIGH,
                LocalDateTime.now().plusDays(1)
        );
        sampleTask.setId(1L);
    }

    @Test
    void shouldCreateTaskSuccessfully() {
        // Given (Подготовка)
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        // When (Действие)
        Task result = taskService.createTask(validRequest);

        // Then (Проверка)
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals(Status.TODO, result.getStatus());

        // Проверяем, что метод save был вызван ровно 1 раз
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldFindTaskById() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        // When
        Task result = taskService.getTaskById(1L);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTaskById(999L);
        });

        verify(taskRepository).findById(999L);
    }

    @Test
    void shouldFindAllTasks() {
        // Given
        Task task1 = createTask("Task 1");
        Task task2 = createTask("Task 2");
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        // When
        List<Task> result = taskService.getAllTasks();

        // Then
        assertEquals(2, result.size());
        verify(taskRepository).findAll();
    }

    @Test
    void shouldFindTasksByStatus() {
        // Given
        Task todoTask = createTask("TODO Task");
        todoTask.setStatus(Status.TODO);
        when(taskRepository.findByStatus(Status.TODO)).thenReturn(List.of(todoTask));

        // When
        List<Task> result = taskService.getTasksByStatus(Status.TODO);

        // Then
        assertEquals(1, result.size());
        assertEquals("TODO Task", result.get(0).getTitle());
        verify(taskRepository).findByStatus(Status.TODO);
    }

    @Test
    void shouldDeleteTask() {
        // Given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.deleteById(1L)).thenReturn(true);

        // When
        taskService.deleteTask(1L);

        // Then
        verify(taskRepository).findById(1L); // Сначала проверили существование
        verify(taskRepository).deleteById(1L); // Потом удалили
    }

    @Test
    void shouldNotDeleteNonExistentTask() {
        // Given
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(999L);
        });

        // Проверяем, что deleteById НЕ был вызван
        verify(taskRepository, never()).deleteById(any());
    }

    // Вспомогательный метод
    private Task createTask(String title) {
        return Task.createNew(
                title,
                "Description",
                Priority.MEDIUM,
                LocalDateTime.now().plusDays(1)
        );
    }
}