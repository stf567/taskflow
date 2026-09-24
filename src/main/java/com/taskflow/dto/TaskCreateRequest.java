package com.taskflow.dto;

import com.taskflow.domain.Priority;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskCreateRequest {

    @NotBlank(message = "Название задачи не может быть пустым")
    @Size(max = 100, message = "Название не должно превышать 100 символов")
    private String title;

    @Size(max = 1000, message = "Описание не должно превышать 1000 символов")
    private String description;

    @NotNull(message = "Приоритет должен быть указан")
    private Priority priority;

    @NotNull(message = "Дедлайн должен быть указан")
    @Future(message = "Дедлайн не может быть в прошлом")
    private LocalDateTime deadline;
}
