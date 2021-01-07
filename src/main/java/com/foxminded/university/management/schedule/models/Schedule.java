package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.util.Objects;

public class Schedule implements BaseEntity<Long> {
    private Long id;
    private Long universityId;

    public Schedule() {
    }

    public Schedule(Long universityId) {
        this.universityId = universityId;
    }

    public Schedule(Long id, Long universityId) {
        this.id = id;
        this.universityId = universityId;
    }

    public Long getUniversityId() {
        return universityId;
    }

    public void setUniversityId(Long universityId) {
        this.universityId = universityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(id, schedule.id) && Objects.equals(universityId, schedule.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, universityId);
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", universityId=" + universityId +
                '}';
    }
}
