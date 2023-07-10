package ru.job4j.todo.repository;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskRepository {

    void save(Task task);

    Collection<Task> getAll();

    Collection<Task> getByStatus(boolean status);

    Optional<Task> getById(int id);

    void update(Task task);

    void updateTaskStatus(int id);

    void deleteById(int id);

}
