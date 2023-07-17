package ru.job4j.todo.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class HibernateUserRepositoryTest {
    private static UserRepository hibernateUserRepository;
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void initRepository() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        hibernateUserRepository = new HibernateUserRepository(new CrudRepository(sessionFactory));
    }

    @BeforeEach
    public void deleteFromDb() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE User").executeUpdate();
            session.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @AfterAll
    public static void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }

    @Test
    public void whenSaveUser() {
        User user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setPassword("password");
        Optional<User> optionalUser = hibernateUserRepository.save(user);

        assertThat(optionalUser.get()).isEqualTo(user);
    }

    @Test
    public void whenFindUserByLoginAndPassword() {
        User user = new User();
        user.setName("name");
        user.setLogin("login");
        user.setPassword("password");
        hibernateUserRepository.save(user);

        Optional<User> optionalUser = hibernateUserRepository.findByLoginAndPassword("login", "password");

        assertThat(optionalUser.get()).isEqualTo(user);
    }
}