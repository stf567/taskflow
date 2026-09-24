package com.taskflow.repository;

import com.taskflow.domain.Status;
import com.taskflow.domain.Task;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory реализация репозитория.
 * Данные хранятся в оперативной памяти и теряются при перезапуске приложения.
 * ВАЖНО: Эта реализация потокобезопасна (thread-safe) благодаря использованию
 * ConcurrentHashMap и AtomicLong.
 */
@Repository
public class InMemoryTaskRepository implements TaskRepository {

    // Хранилище задач: ID -> Task
    // ConcurrentHashMap обеспечивает потокобезопасность при одновременном доступе
    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    // Генератор уникальных ID
    // AtomicLong обеспечивает потокобезопасную инкрементацию
    private final AtomicLong idCounter = new AtomicLong(1);

    @Override
    public Task save(Task task) {
        if (task.getId() == null) {
            // Новая задача — присваиваем ID
            task.setId(idCounter.getAndIncrement());
        }
        // Сохраняем в хранилище (put потокобезопасен в ConcurrentHashMap)
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Optional<Task> findById(Long id) {
        // get возвращает null если ключ не найден, оборачиваем в Optional
        return Optional.ofNullable(tasks.get(id));
    }

    @Override
    public List<Task> findAll() {
        // values() возвращает коллекцию, конвертируем в список
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> findByStatus(Status status) {
        // Используем Stream API для фильтрации
        return tasks.values().stream()
                .filter(task -> task.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        // remove возвращает удаленный объект или null
        return tasks.remove(id) != null;
    }
}
