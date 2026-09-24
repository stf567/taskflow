package com.taskflow.service;

import com.taskflow.domain.Status;
import com.taskflow.domain.Task;
import com.taskflow.dto.TaskCreateRequest;

import java.util.List;

public interface TaskService {
    Task createTask(TaskCreateRequest request);
    Task getTaskById(Long id);
    List<Task> getAllTasks();
    List<Task> getTasksByStatus(Status status);
    void deleteTask(Long id);
}