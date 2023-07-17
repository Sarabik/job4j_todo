package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.*;

@Repository
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<Task> save(Task task) {
        return Optional.ofNullable(crudRepository.getOne(task));
    }

    @Override
    public Collection<Task> getAll() {
        return crudRepository.query("FROM Task ORDER BY id", Task.class);
    }

    @Override
    public Collection<Task> getByStatus(boolean status) {
        return crudRepository.query(
                "FROM Task WHERE done = :fStatus ORDER BY id", Task.class, Map.of("fStatus", status));
    }

    @Override
    public Optional<Task> getById(int id) {
        Task foundTask = crudRepository.getOne(
                "FROM Task WHERE id = :fId", Task.class, Map.of("fId", id));
        return Optional.ofNullable(foundTask);
    }

    @Override
    public boolean update(Task task) {
        return crudRepository.ifChanged(task) != null;
    }

    @Override
    public boolean updateTaskStatus(int id) {
        Boolean ifUpdated = crudRepository.ifChanged(
                "UPDATE Task SET done = true WHERE id = :fId", Map.of("fId", id));
        return ifUpdated != null;
    }

    @Override
    public boolean deleteById(int id) {
        Boolean ifUpdated = crudRepository.ifChanged(
                "DELETE Task WHERE id = :fId", Map.of("fId", id));
        return ifUpdated != null;
    }
}
