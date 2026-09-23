package org.example.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private Long id;
    private String title;
    private String description;
    private Priority priority;
    private Status status;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Task createNew(String title, String description, Priority priority, LocalDateTime deadline) {
        LocalDateTime now = LocalDateTime.now();
        return Task.builder()
                .title(title)
                .description(description)
                .priority(priority)
                .status(Status.TODO) // Новая задача всегда начинается с TODO
                .deadline(deadline)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void touch() {
        this.updatedAt = LocalDateTime.now();
    }
}
