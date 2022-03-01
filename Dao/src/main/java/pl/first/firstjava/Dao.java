package pl.first.firstjava;

import java.sql.SQLException;

public interface Dao<T> extends AutoCloseable {
    T read() throws SQLException;

    void write(T obj) throws SQLException;
}
