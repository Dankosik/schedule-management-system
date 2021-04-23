package com.foxminded.university.management.schedule.dto.lesson;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Subject;

import java.sql.Time;
import java.time.Duration;
import java.util.List;

public class BaseLessonDto {
    private Long id;
    private Integer number;
    private Time startTime;
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
