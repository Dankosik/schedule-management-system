package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Student extends Person {
    private Long id;
    private Integer courseNumber;
    private Long groupId;
    private Long facultyId;
    private Long universityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(Long facultyId) {
        this.facultyId = facultyId;
    }

    public Integer getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(groupId, student.groupId) && Objects.equals(facultyId, student.facultyId) && Objects.equals(courseNumber, student.courseNumber) && Objects.equals(universityId, student.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupId, facultyId, courseNumber, universityId);
    }
}
