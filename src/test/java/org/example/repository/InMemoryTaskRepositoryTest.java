package org.example.repository;

import org.example.domain.Priority;
import org.example.domain.Status;
import org.example.domain.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskRepositoryTest {

    private InMemoryTaskRepository repository;

    @BeforeEach
    void setUp() {
        // Создаем новый чистый экземпляр репозитория перед каждым тестом
        repository = new InMemoryTaskRepository();
    }

    @Test
    void shouldSaveAndFindTaskById() {
        // Given
        Task task = Task.createNew(
                "Test Task",
                "Description",
                Priority.HIGH,
                LocalDateTime.now().plusDays(1)
        );

        // When
        Task saved = repository.save(task);
        Optional<Task> found = repository.findById(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals("Test Task", found.get().getTitle());
        assertEquals(Status.TODO, found.get().getStatus());
    }

    @Test
    void shouldReturnEmptyOptionalWhenTaskNotFound() {
        // When
        Optional<Task> found = repository.findById(999L);

        // Then
        assertFalse(found.isPresent());
    }

    @Test
    void shouldFindAllTasks() {
        // Given
        repository.save(createTask("Task 1"));
        repository.save(createTask("Task 2"));
        repository.save(createTask("Task 3"));

        // When
        List<Task> allTasks = repository.findAll();

        // Then
        assertEquals(3, allTasks.size());
    }

    @Test
    void shouldFindTasksByStatus() {
        // Given
        Task todoTask = createTask("TODO Task");
        todoTask.setStatus(Status.TODO);
        repository.save(todoTask);

        Task doneTask = createTask("DONE Task");
        doneTask.setStatus(Status.DONE);
        repository.save(doneTask);

        // When
        List<Task> todoTasks = repository.findByStatus(Status.TODO);

        // Then
        assertEquals(1, todoTasks.size());
        assertEquals("TODO Task", todoTasks.get(0).getTitle());
    }

    @Test
    void shouldDeleteTaskById() {
        // Given
        Task task = repository.save(createTask("Task to delete"));

        // When
        boolean deleted = repository.deleteById(task.getId());

        // Then
        assertTrue(deleted);
        assertFalse(repository.findById(task.getId()).isPresent());
    }

    @Test
    void shouldReturnFalseWhenDeletingNonExistentTask() {
        // When
        boolean deleted = repository.deleteById(999L);

        // Then
        assertFalse(deleted);
    }

    // Вспомогательный метод (теперь он точно внутри класса!)
    private Task createTask(String title) {
        return Task.createNew(
                title,
                "Description",
                Priority.MEDIUM,
                LocalDateTime.now().plusDays(1)
        );
    }
}
