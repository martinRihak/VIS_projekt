package ORB_folder;

import java.sql.SQLException;

@FunctionalInterface
public interface ValueLoader<T> {
    T load() throws SQLException;
}
