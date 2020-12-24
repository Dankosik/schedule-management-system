package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class Faculty {
    private Long id;
    private String name;
    private Long universityId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        Faculty faculty = (Faculty) o;
        return Objects.equals(id, faculty.id) && Objects.equals(name, faculty.name) &&
                Objects.equals(universityId, faculty.universityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, universityId);
    }
}
