package com.foxminded.university.management.schedule.dto.lesson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.models.validators.DurationConstraint;
import com.foxminded.university.management.schedule.models.validators.StartTime;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.Duration;

public class LessonUpdateDto implements LessonDto {
    @Schema(description = "Unique identifier of the Lesson.",
            example = "1", required = true)
    @NotNull
    private Long id;
    @Schema(description = "Number of the lesson.",
            example = "2", required = true)
    @NotNull
    @Min(value = 1, message = "Must be at least 1")
    @Max(value = 8, message = "Must be no more than 8")
    private Integer number;
    @Schema(description = "Start time of the lesson.",
            example = "13:30:00", required = true)
    @StartTime
    private Time startTime;
    @Schema(description = "Duration of the lesson.",
            example = "PT1H30M", required = true)
    @DurationConstraint
    private Duration duration;
    @NotNull
    private SubjectUpdateDto subject;

    @JsonCreator
    public LessonUpdateDto(@JsonProperty(required = true) Long id, @JsonProperty(required = true) Integer number,
                           @JsonProperty(required = true) Time startTime, @JsonProperty(required = true) Duration duration,
                           @JsonProperty(required = true) SubjectUpdateDto subject) {
        this.id = id;
        this.number = number;
        this.startTime = startTime;
        this.duration = duration;
        this.subject = subject;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Integer getNumber() {
        return number;
    }

    @Override
    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public Time getStartTime() {
        return startTime;
    }

    @Override
    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    @Override
    public SubjectUpdateDto getSubject() {
        return subject;
    }

    @Override
    public void setSubject(SubjectUpdateDto subject) {
        this.subject = subject;
    }
}
