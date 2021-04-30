package com.foxminded.university.management.schedule.dto.lesson;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Subject;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Time;
import java.time.Duration;
import java.util.List;

public class BaseLessonDto {
    @Schema(description = "Unique identifier of the Lesson.",
            example = "1", required = true)
    private Long id;
    @Schema(description = "Number of the lesson.",
            example = "2", required = true)
    private Integer number;
    @Schema(description = "Start time of the lesson.",
            example = "13:30:00", required = true)
    private Time startTime;
    @Schema(description = "Duration of the lesson.",
            example = "PT1H30M", required = true)
    private Duration duration;
    private Subject subject;
    private List<Lecture> lectures;

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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
