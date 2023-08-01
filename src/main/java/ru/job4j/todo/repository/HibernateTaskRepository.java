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
        Optional<Task> optionalTask = Optional.empty();
        try {
            optionalTask = crudRepository.getOptional(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalTask;
    }

    @Override
    public Collection<Task> getAll() {
        return crudRepository.query("FROM Task t JOIN FETCH t.priority ORDER BY t.id", Task.class);
    }

    @Override
    public Collection<Task> getByStatus(boolean status) {
        return crudRepository.query(
                "FROM Task  t JOIN FETCH t.priority WHERE done = :fStatus ORDER BY t.id",
                Task.class, Map.of("fStatus", status));
    }

    @Override
    public Optional<Task> getById(int id) {
        Optional<Task> optionalTask = Optional.empty();
        try {
            optionalTask = crudRepository.getOptional(
                    "FROM Task t JOIN FETCH t.priority JOIN FETCH t.categories WHERE t.id = :fId",
                    Task.class, Map.of("fId", id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalTask;
    }

    @Override
    public boolean update(Task task) {
        boolean ifUpdated = false;
        try {
            ifUpdated = crudRepository.ifChanged(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ifUpdated;
    }

    @Override
    public boolean updateTaskStatus(int id) {
        boolean ifUpdated = false;
        try {
            ifUpdated = crudRepository.ifChanged(
                    "UPDATE Task SET done = true WHERE id = :fId", Map.of("fId", id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ifUpdated;
    }

    @Override
    public boolean deleteById(int id) {
        boolean ifDeleted = false;
        try {
            ifDeleted = crudRepository.ifChanged(
                    "DELETE Task WHERE id = :fId", Map.of("fId", id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ifDeleted;
    }
}
