package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Student extends Person {
    private Long id;
    private Integer courseNumber;
    private Long groupId;
    private Long facultyId;
    private Long universityId;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, String middleName, Integer courseNumber, Long groupId, Long facultyId, Long universityId) {
        super(firstName, lastName, middleName);
        this.id = id;
        this.courseNumber = courseNumber;
        this.groupId = groupId;
        this.facultyId = facultyId;
        this.universityId = universityId;
    }

    public Student(String firstName, String lastName, String middleName, Integer courseNumber, Long groupId, Long facultyId, Long universityId) {
        super(firstName, lastName, middleName);
        this.courseNumber = courseNumber;
        this.groupId = groupId;
        this.facultyId = facultyId;
        this.universityId = universityId;
    }

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
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(courseNumber, student.courseNumber) && Objects.equals(groupId, student.groupId) && Objects.equals(facultyId, student.facultyId) && Objects.equals(universityId, student.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, courseNumber, groupId, facultyId, universityId);
    }
}
