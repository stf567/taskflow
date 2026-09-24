package org.example.repository;


import org.example.domain.Task;
import org.example.domain.Status;
import java.util.List;
import java.util.Optional;


public interface TaskRepository {

    /**
     * Сохранить задачу (создать или обновить).
     * @return сохраненная задача с присвоенным ID
     */
    Task save(Task task);

    /**
     * Найти задачу по ID.
     * @return Optional с задачей, если найдена
     */
    Optional<Task> findById(Long id);


    List<Task> findAll();

    /**
     * Найти задачи по статусу.
     * @param status статус для фильтрации
     * @return список задач с указанным статусом
     */
    List<Task> findByStatus(Status status);

    /**
     * Удалить задачу по ID.
     * @param id идентификатор задачи
     * @return true если задача была удалена, false если не найдена
     */
    boolean deleteById(Long id);
}
