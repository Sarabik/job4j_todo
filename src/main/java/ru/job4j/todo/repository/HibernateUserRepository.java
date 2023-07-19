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
        Optional<User> optionalUser;
        try {
            optionalUser = crudRepository.getOptional(user);
        } catch (Exception e) {
            optionalUser = Optional.empty();
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> optionalUser;
        try {
            optionalUser = crudRepository.getOptional(
                    "FROM User WHERE login = :fLogin AND password = :fPassword", User.class,
                    Map.of("fLogin", login, "fPassword", password));
        } catch (Exception e) {
            optionalUser = Optional.empty();
        }
        return optionalUser;
    }
}
