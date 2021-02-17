package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.util.Objects;

public class Student extends Person implements BaseEntity<Long> {
    private Long id;
    private Integer courseNumber;
    private Long groupId;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, String middleName, Integer courseNumber, Long groupId) {
        super(firstName, lastName, middleName);
        this.id = id;
        this.courseNumber = courseNumber;
        this.groupId = groupId;
    }

    public Student(String firstName, String lastName, String middleName, Integer courseNumber, Long groupId) {
        super(firstName, lastName, middleName);
        this.courseNumber = courseNumber;
        this.groupId = groupId;
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
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(courseNumber, student.courseNumber) && Objects.equals(groupId, student.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), courseNumber, groupId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", courseNumber=" + courseNumber +
                ", groupId=" + groupId +
                '}';
    }
}
