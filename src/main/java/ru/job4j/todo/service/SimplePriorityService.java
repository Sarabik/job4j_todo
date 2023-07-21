package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Priority;
import ru.job4j.todo.repository.PriorityRepository;

import java.util.Collection;

@Service
@AllArgsConstructor
public class SimplePriorityService implements PriorityService {

    public final PriorityRepository priorityRepository;

    @Override
    public Collection<Priority> getAll() {
        return priorityRepository.getAll();
    }
}
