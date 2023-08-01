package ru.job4j.todo.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.repository.CategoryRepository;

import java.util.Collection;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SimpleCategoryService implements CategoryService {

    public final CategoryRepository categoryRepository;

    @Override
    public Collection<Category> getAll() {
        return categoryRepository.getAll();
    }

    @Override
    public Optional<Category> getById(int id) {
        return categoryRepository.getById(id);
    }

}
