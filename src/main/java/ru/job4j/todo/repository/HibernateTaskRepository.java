package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class HibernateTaskRepository implements TaskRepository {

    private final SessionFactory sf;

    @Override
    public void save(Task task) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.persist(task);
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public Collection<Task> getAll() {
        List<Task> list = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            list = session.createQuery("FROM Task ORDER BY id", Task.class).list();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public Collection<Task> getByStatus(boolean status) {
        List<Task> list = new ArrayList<>();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            list = session.createQuery("FROM Task WHERE done = :fStatus ORDER BY id", Task.class)
                    .setParameter("fStatus", status)
                    .list();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return list;
    }

    @Override
    public Optional<Task> getById(int id) {
        Optional<Task> optionalTask = Optional.empty();
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            optionalTask = session.createQuery("FROM Task WHERE id = :fId", Task.class)
                    .setParameter("fId", id)
                    .uniqueResultOptional();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return optionalTask;
    }

    @Override
    public boolean update(Task task) {
        int changed = 0;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            String sql = """
                    UPDATE Task SET title = :fTitle, 
                    description = :fDescription, created = :fCreated, 
                    done = :fDone WHERE id = :fId
                    """;
            changed = session.createQuery(sql)
                    .setParameter("fTitle", task.getTitle())
                    .setParameter("fDescription", task.getDescription())
                    .setParameter("fCreated", task.getCreated())
                    .setParameter("fDone", task.isDone())
                    .setParameter("fId", task.getId())
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return changed > 0;
    }

    @Override
    public boolean updateTaskStatus(int id) {
        int changed = 0;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            changed = session.createQuery("UPDATE Task SET done = true WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return changed > 0;
    }

    @Override
    public boolean deleteById(int id) {
        int changed = 0;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            changed = session.createQuery("DELETE Task WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return changed > 0;
    }
}
