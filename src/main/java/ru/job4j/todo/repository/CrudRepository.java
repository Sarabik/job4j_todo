package ru.job4j.todo.repository;

import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@AllArgsConstructor
public class CrudRepository {

    private final SessionFactory sf;

    public <T> T tx(Function<Session, T> command) {
        Session session = sf.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            T result = command.apply(session);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public <T> Optional<T> getOptional(T t) {
        Function<Session, Optional<T>> command = session -> {
            session.persist(t);
            return Optional.of(t);
        };
        return tx(command);
    }

    public <T> Optional<T> getOptional(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, Optional<T>> command = session -> {
            Query<T> q = session.createQuery(query, cl);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
            return q.uniqueResultOptional();
        };
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl) {
        Function<Session, List<T>> command = session -> session
                .createQuery(query, cl)
                .list();
        return tx(command);
    }

    public <T> List<T> query(String query, Class<T> cl, Map<String, Object> args) {
        Function<Session, List<T>> command = session -> {
            Query<T> q = session.createQuery(query, cl);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
            return q.list();
        };
        return tx(command);
    }

    public <T> boolean ifChanged(T t) {
        Function<Session, Boolean> command = session -> {
            session.update(t);
            return true;
        };
        return tx(command);
    }

    public boolean ifChanged(String query, Map<String, Object> args) {
        Function<Session, Boolean> command = session -> {
            var q = session.createQuery(query);
            for (Map.Entry<String, Object> entry : args.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
            return q.executeUpdate() > 0;
        };
        return tx(command);
    }
}
