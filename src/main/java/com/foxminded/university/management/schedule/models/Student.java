package com.foxminded.university.management.schedule.models;

import java.util.List;
import java.util.Objects;

public class Student extends Person {
    private Group group;
    private Faculty faculty;
    private Integer courseNumber;
    private List<Teacher> teachers;
    private List<Subject> subjects;

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

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(group, student.group) && Objects.equals(faculty, student.faculty) &&
                Objects.equals(courseNumber, student.courseNumber) &&
                Objects.equals(teachers, student.teachers) && Objects.equals(subjects, student.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), group, faculty, courseNumber, teachers, subjects);
    }
}
