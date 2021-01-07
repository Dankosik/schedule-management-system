package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.util.Objects;

public class Group implements BaseEntity<Long> {
    private Long id;
    private String name;
    private Long lectureId;
    private Long departmentId;
    private Long facultyId;
    private Long universityId;

    public Group() {
    }

    public Group(Long id, String name, Long lectureId, Long departmentId, Long facultyId, Long universityId) {
        this.id = id;
        this.name = name;
        this.lectureId = lectureId;
        this.departmentId = departmentId;
        this.facultyId = facultyId;
        this.universityId = universityId;
    }

    public Group(String name, Long lectureId, Long departmentId, Long facultyId, Long universityId) {
        this.name = name;
        this.lectureId = lectureId;
        this.departmentId = departmentId;
        this.facultyId = facultyId;
        this.universityId = universityId;
    }

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

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lectureId=" + lectureId +
                ", departmentId=" + departmentId +
                ", facultyId=" + facultyId +
                ", universityId=" + universityId +
                '}';
    }
}
