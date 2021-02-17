package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.util.Objects;

public class Audience implements BaseEntity<Long> {
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

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
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
        return Objects.equals(number, audience.number) && Objects.equals(capacity, audience.capacity) && Objects.equals(universityId, audience.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, capacity, universityId);
    }

    @Override
    public String toString() {
        return "Audience{" +
                "id=" + id +
                ", number=" + number +
                ", capacity=" + capacity +
                ", universityId=" + universityId +
                '}';
    }
}
