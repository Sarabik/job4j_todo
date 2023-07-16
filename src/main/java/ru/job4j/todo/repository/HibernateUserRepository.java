package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateUserRepository implements UserRepository {

    private final SessionFactory sf;

    @Override
    public Optional<User> save(User user) {
        Optional<User> optionalUser = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
            optionalUser = Optional.of(user);
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return optionalUser;
    }

    @Override
    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> optionalUser = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            optionalUser = session.createQuery(
                    "FROM User WHERE login = :fLogin AND password = :fPassword", User.class)
                    .setParameter("fLogin", login)
                    .setParameter("fPassword", password)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return optionalUser;
    }
}
