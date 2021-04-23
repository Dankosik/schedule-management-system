package com.foxminded.university.management.schedule.dto.lesson;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.models.validators.DurationConstraint;
import com.foxminded.university.management.schedule.models.validators.StartTime;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.Duration;

public class LessonAddDto implements LessonDto {
    @NotNull
    @Min(value = 1, message = "Must be at least 1")
    @Max(value = 8, message = "Must be no more than 8")
    private Integer number;
    @StartTime
    private Time startTime;
    @DurationConstraint
    private Duration duration;
    @NotNull
    private SubjectUpdateDto subject;

    @JsonCreator
    public LessonAddDto(@JsonProperty(required = true) Integer number, @JsonProperty(required = true) Time startTime,
                        @JsonProperty(required = true) Duration duration, @JsonProperty(required = true) SubjectUpdateDto subject) {
        this.number = number;
        this.startTime = startTime;
        this.duration = duration;
        this.subject = subject;
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
