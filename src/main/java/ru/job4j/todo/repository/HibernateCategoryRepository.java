package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class HibernateCategoryRepository implements CategoryRepository {

    private final CrudRepository crudRepository;

    @Override
    public Collection<Category> getAll() {
        return crudRepository.query("FROM Category", Category.class);
    }

    @Override
    public Collection<Category> getSomeById(List<Integer> idList) {
        return crudRepository.query("FROM Category c WHERE c.id IN :cCategoryList",
                Category.class, Map.of("cCategoryList", idList));
    }

}