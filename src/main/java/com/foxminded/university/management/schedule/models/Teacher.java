package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.util.Objects;

public class Teacher extends Person implements BaseEntity<Long> {
    private Long id;
    private Long universityId;

    public Teacher() {

    }

    public Teacher(Long id, String firstName, String lastName, String middleName, Long universityId) {
        super(firstName, lastName, middleName);
        this.id = id;
        this.universityId = universityId;
    }

    public Teacher(String firstName, String lastName, String middleName, Long universityId) {
        super(firstName, lastName, middleName);
        this.universityId = universityId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(universityId, teacher.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), universityId);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstName=" + super.getFirstName() +
                ", lastName=" + super.getLastName() +
                ", middleName=" + super.getMiddleName() +
                ", universityId=" + universityId +
                '}';
    }
}
