package com.taskflow.controller;

import com.taskflow.domain.Status;
import com.taskflow.domain.Task;
import com.taskflow.dto.TaskCreateRequest;
import com.taskflow.dto.TaskResponse;
import com.taskflow.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    // Constructor Injection
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * POST /api/tasks
     * Создание новой задачи
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        Task createdTask = taskService.createTask(request);
        return ResponseEntity
                .status(HttpStatus.CREATED) // 201 Created
                .body(TaskResponse.from(createdTask));
    }

    /**
     * GET /api/tasks/{id}
     * Получение задачи по ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id); // Если не найдет, GlobalExceptionHandler перехватит исключение
        return ResponseEntity.ok(TaskResponse.from(task)); // 200 OK
    }

    /**
     * GET /api/tasks
     * Получение всех задач
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/tasks/status/{status}
     * Фильтрация задач по статусу
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(@PathVariable Status status) {
        List<Task> tasks = taskService.getTasksByStatus(status);
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/tasks/{id}
     * Удаление задачи
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build(); // 204 No Content (успешно удалено, тело пустое)
    }
}