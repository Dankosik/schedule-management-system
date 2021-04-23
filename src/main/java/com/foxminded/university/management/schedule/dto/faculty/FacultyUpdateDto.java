package com.foxminded.university.management.schedule.dto.faculty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.models.validators.FacultyName;

import javax.validation.constraints.NotNull;

public class FacultyUpdateDto {
    @NotNull
    private Long id;
    @FacultyName
    private String name;

    @JsonCreator
    public FacultyUpdateDto(@JsonProperty(required = true) Long id, @JsonProperty(required = true) String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
