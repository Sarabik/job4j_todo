package ru.job4j.todo.service;

import ru.job4j.todo.model.Task;

import java.util.Collection;
import java.util.Optional;

public interface TaskService {
    Optional<Task> save(Task task);

    Collection<Task> getAll();

    Collection<Task> getByStatus(boolean status);

    Optional<Task> getById(int id);

    boolean update(Task task);

    boolean updateTaskStatus(int id);

    boolean deleteById(int id);
}
