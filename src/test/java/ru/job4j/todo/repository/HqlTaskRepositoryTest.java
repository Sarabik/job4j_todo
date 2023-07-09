package ru.job4j.todo.repository;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.job4j.todo.model.Task;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

class HqlTaskRepositoryTest {
    private static TaskRepository hqlTaskRepository;
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void initRepository() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        hqlTaskRepository = new HqlTaskRepository(sessionFactory);
    }

    @BeforeEach
    public void deleteFromDb() {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.createQuery("DELETE Task").executeUpdate();
            session.createNativeQuery("ALTER TABLE tasks ALTER COLUMN id RESTART WITH 1").executeUpdate();
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
    public void whenSaveTaskThenGetItById() {
        Task task1 = new Task();
        task1.setDescription("Write a book");
        task1.setCreated(LocalDateTime.now());
        hqlTaskRepository.save(task1);
        Optional<Task> optionalTask = hqlTaskRepository.getById(1);

        assertThat(optionalTask.get()).isEqualTo(task1);
    }

    @Test
    public void whenDidFindTaskThenGetEmptyOptional() {
        Optional<Task> optionalTask = hqlTaskRepository.getById(2);

        assertThat(optionalTask).isEmpty();
    }

    @Test
    public void whenGetAllTasksThenGetCollection() {
        Task task1 = new Task();
        task1.setDescription("Write a book");
        task1.setCreated(LocalDateTime.now());
        Task task2 = new Task();
        task2.setDescription("Clean the room");
        task2.setCreated(LocalDateTime.now());
        hqlTaskRepository.save(task1);
        hqlTaskRepository.save(task2);

        Collection<Task> collection = hqlTaskRepository.getAll();

        assertThat(collection).containsExactly(task1, task2);
    }

    @Test
    public void whenGetTasksByStatusThenGetCollection() {
        Task task1 = new Task();
        task1.setDescription("Write a book");
        task1.setCreated(LocalDateTime.now());
        Task task2 = new Task();
        task2.setDescription("Clean the room");
        task2.setCreated(LocalDateTime.now());
        task2.setDone(true);
        hqlTaskRepository.save(task1);
        hqlTaskRepository.save(task2);

        Collection<Task> notDone = hqlTaskRepository.getByStatus(false);
        Collection<Task> done = hqlTaskRepository.getByStatus(true);

        assertThat(notDone).containsExactly(task1);
        assertThat(done).containsExactly(task2);
    }

    @Test
    public void whenDeleteByIdThenGetEmptyCollection() {
        Task task1 = new Task();
        task1.setDescription("Write a book");
        task1.setCreated(LocalDateTime.now());
        hqlTaskRepository.save(task1);

        hqlTaskRepository.deleteById(1);

        assertThat(hqlTaskRepository.getAll()).isEmpty();
    }
}