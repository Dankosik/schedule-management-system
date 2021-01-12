package com.foxminded.university.management.schedule.dao;

public abstract class AbstractDao<T extends BaseEntity<Long>> implements Dao<T, Long> {
    @Override
    public T save(T object) {
        return object.getId() == null ? create(object) : update(object);
    }

    protected abstract T update(T object);

    protected abstract T create(T object);

}