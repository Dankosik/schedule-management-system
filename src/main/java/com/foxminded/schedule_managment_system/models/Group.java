package com.foxminded.schedule_managment_system.models;

import java.util.List;
import java.util.Objects;

public class Group {
    private List<Student> students;
    private String name;

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(students, group.students) && Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(students, name);
    }
}
