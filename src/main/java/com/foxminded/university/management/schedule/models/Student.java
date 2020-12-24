package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Student extends Person {
    private Long id;
    private Long groupId;
    private Group group;
    private Faculty faculty;
    private Integer courseNumber;
    private Long universityId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
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
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(groupId, student.groupId) &&
                Objects.equals(group, student.group) && Objects.equals(faculty, student.faculty) &&
                Objects.equals(courseNumber, student.courseNumber) && Objects.equals(universityId, student.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, groupId, group, faculty, courseNumber, universityId);
    }
}
