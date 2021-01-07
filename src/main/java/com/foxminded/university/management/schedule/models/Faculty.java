package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.util.Objects;

public class Faculty implements BaseEntity<Long> {
    private Long id;
    private String name;
    private Long universityId;

    public Faculty() {

    }

    public Faculty(Long id, String name, Long universityId) {
        this.id = id;
        this.name = name;
        this.universityId = universityId;
    }

    public Faculty(String name, Long universityId) {
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
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) &&
                Objects.equals(universityId, faculty.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, universityId);
    }

    @Override
    public String toString() {
        return "Faculty{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", universityId=" + universityId +
                '}';
    }
}
