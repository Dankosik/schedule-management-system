package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Audience {
    private Long id;
    private Integer number;
    private Integer capacity;
    private Long universityId;

    public Audience() {
    }

    public Audience(Long id, Integer number, Integer capacity, Long universityId) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.universityId = universityId;
    }

    public Audience(Integer number, Integer capacity, Long universityId) {
        this.number = number;
        this.capacity = capacity;
        this.universityId = universityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audience audience = (Audience) o;
        return Objects.equals(id, audience.id) && Objects.equals(number, audience.number) &&
                Objects.equals(capacity, audience.capacity) && Objects.equals(universityId, audience.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, capacity, universityId);
    }
}
