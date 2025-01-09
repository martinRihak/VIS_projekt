package com.moravianwine.app.service.ValueHolder;

import java.sql.SQLException;

@FunctionalInterface
public interface ValueLoader<T> {
    T load() throws SQLException;
}
