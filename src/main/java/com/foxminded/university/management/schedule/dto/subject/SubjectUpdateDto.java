package com.foxminded.university.management.schedule.dto.subject;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.models.validators.SubjectName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class SubjectUpdateDto {
    @Schema(description = "Unique identifier of the Subject.",
            example = "1", required = true)
    @NotNull
    private Long id;
    @Schema(description = "Name of the subject.",
            example = "Math", required = true)
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
