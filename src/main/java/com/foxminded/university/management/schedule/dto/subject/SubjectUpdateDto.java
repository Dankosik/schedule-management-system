package com.foxminded.university.management.schedule.dto.subject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.models.validators.SubjectName;

import javax.validation.constraints.NotNull;

public class SubjectUpdateDto {
    @NotNull
    private Long id;
    @SubjectName
    private String name;

    @JsonCreator
    public SubjectUpdateDto(@JsonProperty(required = true) Long id, @JsonProperty(required = true) String name) {
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
