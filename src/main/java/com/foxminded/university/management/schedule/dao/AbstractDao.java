package com.foxminded.university.management.schedule.dao;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;

public abstract class AbstractDao<T> implements Dao<T> {
    @Override
    public T save(T object) {
        try {
            if (object.getClass().getMethod("getId").invoke(object) == null) return create(object);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return update(object);
    }

    protected abstract T create(T object);

    protected abstract T update(T object);
}
