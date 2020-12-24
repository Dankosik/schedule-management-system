package com.foxminded.university.management.schedule.models;

import java.util.List;
import java.util.Objects;

public class Teacher extends Person {
    private List<Lecture> lectures;
    private List<Student> students;

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(lectures, teacher.lectures) && Objects.equals(students, teacher.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lectures, students);
    }
}
