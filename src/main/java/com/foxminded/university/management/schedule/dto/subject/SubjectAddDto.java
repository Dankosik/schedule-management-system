package com.foxminded.university.management.schedule.dto.subject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.models.validators.SubjectName;

public class SubjectAddDto {
    @SubjectName
    private String name;

    @JsonCreator
    public SubjectAddDto(@JsonProperty(required = true) String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
