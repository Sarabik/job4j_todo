package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.repository.TaskRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleTaskService implements TaskService {

    public final TaskRepository taskRepository;

    @Override
    public void save(Task task) {
        taskRepository.save(task);
    }

    @Override
    public Collection<Task> getAll() {
        return taskRepository.getAll();
    }

    @Override
    public Collection<Task> getByStatus(boolean status) {
        return taskRepository.getByStatus(status);
    }

    @Override
    public Optional<Task> getById(int id) {
        return taskRepository.getById(id);
    }

    @Override
    public boolean update(Task task) {
        return taskRepository.update(task);
    }

    @Override
    public boolean updateTaskStatus(int id) {
        return taskRepository.updateTaskStatus(id);
    }

    @Override
    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }
}
