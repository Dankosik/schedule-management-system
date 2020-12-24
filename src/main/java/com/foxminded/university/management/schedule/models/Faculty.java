package com.foxminded.university.management.schedule.models;

import java.util.List;
import java.util.Objects;

public class Faculty {
    private String name;
    private List<Department> departments;
    private List<Group> groups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Faculty faculty = (Faculty) o;
        return Objects.equals(name, faculty.name) && Objects.equals(departments, faculty.departments)
                && Objects.equals(groups, faculty.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, departments, groups);
    }
}