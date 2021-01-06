package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Subject {
    private Long id;
    private String name;
    private Long universityId;

    public Subject() {

    }

    public Subject(Long id, String name, Long universityId) {
        this.id = id;
        this.name = name;
        this.universityId = universityId;
    }

    public Subject(String name, Long universityId) {
        this.name = name;
        this.universityId = universityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) && Objects.equals(name, subject.name) && Objects.equals(universityId, subject.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, universityId);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", universityId=" + universityId +
                '}';
    }
}
