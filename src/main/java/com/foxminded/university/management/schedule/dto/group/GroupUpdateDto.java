package com.foxminded.university.management.schedule.dto.group;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.models.validators.GroupName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class GroupUpdateDto implements GroupDto {
    @Schema(description = "Unique identifier of the Group.",
            example = "1", required = true)
    @NotNull
    private Long id;
    @Schema(description = "Name of the group.",
            example = "AB-01", required = true)
    @GroupName
    private String name;
    @NotNull
    private FacultyUpdateDto faculty;

    @JsonCreator
    public GroupUpdateDto(@JsonProperty(required = true) Long id, String name, @JsonProperty(required = true) FacultyUpdateDto faculty) {
        this.id = id;
        this.name = name;
        this.faculty = faculty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public FacultyUpdateDto getFaculty() {
        return faculty;
    }

    @Override
    public void setFaculty(FacultyUpdateDto faculty) {
        this.faculty = faculty;
    }
}
