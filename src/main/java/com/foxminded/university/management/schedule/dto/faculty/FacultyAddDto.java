package com.foxminded.university.management.schedule.dto.faculty;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.models.validators.FacultyName;

public class FacultyAddDto {
    @FacultyName
    private String name;

    @JsonCreator
    public FacultyAddDto(@JsonProperty(required = true) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
