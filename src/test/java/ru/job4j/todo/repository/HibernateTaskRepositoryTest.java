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

class HibernateTaskRepositoryTest {
    private static TaskRepository hibernateTaskRepository;
    private static StandardServiceRegistry registry;
    private static SessionFactory sessionFactory;

    @BeforeAll
    public static void initRepository() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        hibernateTaskRepository = new HibernateTaskRepository(new CrudRepository(sessionFactory));
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
        task1.setTitle("Write a book");
        task1.setCreated(LocalDateTime.now());
        Optional<Task> optionalTask = hibernateTaskRepository.save(task1);

        assertThat(optionalTask.get()).isEqualTo(task1);
    }

    @Test
    public void whenDidNotFindTaskThenGetEmptyOptional() {
        Optional<Task> optionalTask = hibernateTaskRepository.getById(2);

        assertThat(optionalTask).isEmpty();
    }

    @Test
    public void whenGetAllTasksThenGetCollection() {
        Task task1 = new Task();
        task1.setTitle("Write a book");
        task1.setCreated(LocalDateTime.now());
        Task task2 = new Task();
        task2.setTitle("Clean the room");
        task2.setCreated(LocalDateTime.now());
        hibernateTaskRepository.save(task1);
        hibernateTaskRepository.save(task2);

        Collection<Task> collection = hibernateTaskRepository.getAll();

        assertThat(collection).containsExactly(task1, task2);
    }

    @Test
    public void whenGetTasksByStatusThenGetCollection() {
        Task task1 = new Task();
        task1.setTitle("Write a book");
        task1.setCreated(LocalDateTime.now());
        Task task2 = new Task();
        task2.setTitle("Clean the room");
        task2.setCreated(LocalDateTime.now());
        task2.setDone(true);
        hibernateTaskRepository.save(task1);
        hibernateTaskRepository.save(task2);

        Collection<Task> notDone = hibernateTaskRepository.getByStatus(false);
        Collection<Task> done = hibernateTaskRepository.getByStatus(true);

        assertThat(notDone).containsExactly(task1);
        assertThat(done).containsExactly(task2);
    }

    @Test
    public void whenUpdateTasksThenGetTasksUpdated() {
        Task task1 = new Task();
        task1.setTitle("Write a book");
        task1.setCreated(LocalDateTime.now());
        hibernateTaskRepository.save(task1);

        Task task2 = new Task();
        task2.setId(task1.getId());
        task2.setTitle("Clean the room");
        hibernateTaskRepository.update(task2);

        assertThat(hibernateTaskRepository.getById(1).get().getTitle()).isEqualTo("Clean the room");
    }

    @Test
    public void whenUpdateTaskStatusThenGetDoneStatus() {
        Task task1 = new Task();
        task1.setTitle("Write a book");
        task1.setCreated(LocalDateTime.now());
        hibernateTaskRepository.save(task1);

        hibernateTaskRepository.updateTaskStatus(1);

        assertThat(hibernateTaskRepository.getById(1).get().isDone()).isTrue();
    }

    @Test
    public void whenDeleteByIdThenGetEmptyCollection() {
        Task task1 = new Task();
        task1.setTitle("Write a book");
        task1.setCreated(LocalDateTime.now());
        hibernateTaskRepository.save(task1);

        hibernateTaskRepository.deleteById(1);

        assertThat(hibernateTaskRepository.getAll()).isEmpty();
    }
}