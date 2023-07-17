package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Map;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final CrudRepository crudRepository;

    @Override
    public Optional<User> save(User user) {
        return Optional.ofNullable(crudRepository.getOne(user));
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        User foundUser = crudRepository.getOne(
                "FROM User WHERE login = :fLogin AND password = :fPassword", User.class,
                Map.of("fLogin", login, "fPassword", password));
        return Optional.ofNullable(foundUser);
    }
}
