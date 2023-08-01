package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> getAll() {
        return crudRepository.query("FROM Category", Category.class);
    }

    @Override
    public Optional<Category> getById(int id) {
        Optional<Category> optionalCategory = Optional.empty();
        try {
            optionalCategory = crudRepository.getOptional(
                    "FROM Category WHERE id = :fId",
                    Category.class, Map.of("fId", id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return optionalCategory;
    }
}