package com.foxminded.university.management.schedule.dto.faculty;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class BaseFacultyDto {
    @Schema(description = "Unique identifier of the Faculty.",
            example = "1", required = true)
    private Long id;
    @Schema(description = "Name of the faculty.",
            example = "FAIT", required = true)
    private String name;
    private List<Group> groups;
    private List<Teacher> teachers;

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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }
}
