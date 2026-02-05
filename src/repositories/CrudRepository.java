package repositories;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CrudRepository<T, ID> {

    void insert(Connection connection, T entity);

    List<T> findAll(Connection connection) throws SQLException;

    T findById(Connection connection, ID id);
}
