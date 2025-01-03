package ORB_folder;

import java.sql.SQLException;

public class ValueHolder <T> {
    private T value;
    private ValueLoader<T> loader;

    public ValueHolder(ValueLoader<T> loader) {
        this.loader = loader;
    }

    public T getValue() throws SQLException {
        if(value == null) {
            value = loader.load();
        }
        return value;
    }
}

