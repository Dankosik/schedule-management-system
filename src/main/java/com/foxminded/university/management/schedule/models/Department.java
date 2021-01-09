package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.util.Objects;

public class Department implements BaseEntity<Long> {
    private Long id;
    private String name;
    private Long facultyId;
    private Long universityId;

    public Department() {

    }

    public Department(Long id, String name, Long facultyId, Long universityId) {
        this.id = id;
        this.name = name;
        this.facultyId = facultyId;
        this.universityId = universityId;
    }

    public Department(String name, Long facultyId, Long universityId) {
        this.name = name;
        this.facultyId = facultyId;
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

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
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
        Department that = (Department) o;
        return Objects.equals(name, that.name) && Objects.equals(facultyId, that.facultyId) && Objects.equals(universityId, that.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, facultyId, universityId);
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", facultyId=" + facultyId +
                ", universityId=" + universityId +
                '}';
    }
}
