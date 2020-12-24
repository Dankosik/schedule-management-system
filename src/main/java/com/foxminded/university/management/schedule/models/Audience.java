package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Audience {
    private int number;
    private int capacity;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audience audience = (Audience) o;
        return number == audience.number && capacity == audience.capacity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, capacity);
    }
}
