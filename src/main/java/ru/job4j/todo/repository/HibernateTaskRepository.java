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
        Optional<Task> optionalTask;
        try {
            optionalTask = crudRepository.getOptional(task);
        } catch (Exception e) {
            optionalTask = Optional.empty();
        }
        return optionalTask;
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
        Optional<Task> optionalTask;
        try {
            optionalTask = crudRepository.getOptional(
                    "FROM Task WHERE id = :fId", Task.class, Map.of("fId", id));
        } catch (Exception e) {
            optionalTask = Optional.empty();
        }
        return optionalTask;
    }

    @Override
    public boolean update(Task task) {
        boolean ifUpdated;
        try {
            ifUpdated = crudRepository.ifChanged(task);
        } catch (Exception e) {
            ifUpdated = false;
        }
        return ifUpdated;
    }

    @Override
    public boolean updateTaskStatus(int id) {
        boolean ifUpdated;
        try {
            ifUpdated = crudRepository.ifChanged(
                    "UPDATE Task SET done = true WHERE id = :fId", Map.of("fId", id));
        } catch (Exception e) {
            ifUpdated = false;
        }
        return ifUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        boolean ifDeleted;
        try {
            ifDeleted = crudRepository.ifChanged(
                    "DELETE Task WHERE id = :fId", Map.of("fId", id));
        } catch (Exception e) {
            ifDeleted = false;
        }
        return ifDeleted;
    }
}
