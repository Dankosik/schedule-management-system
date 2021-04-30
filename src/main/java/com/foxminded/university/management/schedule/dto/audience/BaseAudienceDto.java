package com.foxminded.university.management.schedule.dto.audience;

import com.foxminded.university.management.schedule.models.Lecture;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class BaseAudienceDto {
    @Schema(description = "Unique identifier of the Audience.",
            example = "1", required = true)
    private Long id;
    @Schema(description = "Number of the audience.",
            example = "123", required = true)
    private Integer number;
    @Schema(description = "Capacity of the audience.",
            example = "45", required = true)
    private Integer capacity;
    private List<Lecture> lectures;

    public BaseAudienceDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
