package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Group {
    private Long id;
    private Long lectureId;
    private Long departmentId;
    private Long facultyId;
    private String name;
    private Long universityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public void setLectureId(Long lectureId) {
        this.lectureId = lectureId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
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
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(lectureId, group.lectureId) &&
                Objects.equals(departmentId, group.departmentId) && Objects.equals(facultyId, group.facultyId) &&
                Objects.equals(name, group.name) && Objects.equals(universityId, group.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lectureId, departmentId, facultyId, name, universityId);
    }
}
