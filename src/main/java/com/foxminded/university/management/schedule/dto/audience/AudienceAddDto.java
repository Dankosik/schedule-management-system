package com.foxminded.university.management.schedule.dto.audience;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AudienceAddDto {
    @Schema(description = "Number of the audience.",
            example = "123", required = true)
    @NotNull
    @Min(value = 1, message = "Must be at least 1")
    @Max(value = 999, message = "Must be no more than 999")
    private Integer number;
    @Schema(description = "Capacity of the audience.",
            example = "45", required = true)
    @NotNull
    @Min(value = 1, message = "must be at least 1")
    @Max(value = 999, message = "must be no more than 999")
    private Integer capacity;

    @JsonCreator
    public AudienceAddDto(@JsonProperty(required = true) Integer number, @JsonProperty(required = true) Integer capacity) {
        this.number = number;
        this.capacity = capacity;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

}
