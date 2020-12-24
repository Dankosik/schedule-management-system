package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Subject {
    private Long id;
    private String name;
    private Long studentId;
    private Long teacherId;
    private Long universityId;

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

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
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
        return Objects.equals(id, subject.id) && Objects.equals(name, subject.name) &&
                Objects.equals(studentId, subject.studentId) && Objects.equals(teacherId, subject.teacherId) &&
                Objects.equals(universityId, subject.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, studentId, teacherId, universityId);
    }
}
