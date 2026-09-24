package com.taskflow.service;

import com.taskflow.domain.Status;
import com.taskflow.domain.Task;
import com.taskflow.dto.TaskCreateRequest;
import com.taskflow.exception.TaskNotFoundException;
import com.taskflow.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // <-- Оставили только это. Этого достаточно, чтобы Spring увидел класс
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    // Constructor Injection
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(TaskCreateRequest request) {
        Task newTask = Task.createNew(
                request.getTitle(),
                request.getDescription(),
                request.getPriority(),
                request.getDeadline()
        );
        return taskRepository.save(newTask);
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с ID " + id + " не найдена"));
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByStatus(Status status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public void deleteTask(Long id) {
        getTaskById(id); // Проверяем существование (выкинет исключение, если нет)
        taskRepository.deleteById(id);
    }
}