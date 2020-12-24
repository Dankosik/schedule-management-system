package com.foxminded.university.management.schedule.models;

import java.util.List;
import java.util.Objects;

public class University {
    private List<Student> students;
    private List<Teacher> teachers;
    private List<Subject> subjects;
    private List<Group> groups;
    private List<Faculty> faculties;
    private List<Department> departments;
    private List<Audience> audiences;
    private Schedule schedule;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Faculty> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<Faculty> faculties) {
        this.faculties = faculties;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Audience> getAudiences() {
        return audiences;
    }

    public void setAudiences(List<Audience> audiences) {
        this.audiences = audiences;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        University that = (University) o;
        return Objects.equals(students, that.students) && Objects.equals(teachers, that.teachers) &&
                Objects.equals(subjects, that.subjects) && Objects.equals(groups, that.groups) &&
                Objects.equals(faculties, that.faculties) && Objects.equals(departments, that.departments) &&
                Objects.equals(audiences, that.audiences) && Objects.equals(schedule, that.schedule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, teachers, subjects, groups, faculties, departments, audiences, schedule);
    }
}
