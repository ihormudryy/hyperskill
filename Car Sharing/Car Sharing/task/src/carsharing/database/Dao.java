package carsharing.database;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(int id);

    List<T> getAll();

    void save(T t);

    void update(T t, String[] params);

    void delete(T t);

    void eraseById(Integer id);

    void eraseByName(String name);
}
