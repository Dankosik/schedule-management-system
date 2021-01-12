package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.sql.Time;
import java.time.Duration;
import java.util.Objects;

public class Lesson implements BaseEntity<Long> {
    private Long id;
    private Integer number;
    private Time startTime;
    private Duration duration;
    private Long subjectId;

    public Lesson() {

    }

    public Lesson(Long id, Integer number, Time startTime, Duration duration, Long subjectId) {
        this.id = id;
        this.number = number;
        this.startTime = startTime;
        this.duration = duration;
        this.subjectId = subjectId;
    }

    public Lesson(Integer number, Time startTime, Duration duration, Long subjectId) {
        this.number = number;
        this.startTime = startTime;
        this.duration = duration;
        this.subjectId = subjectId;
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

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(number, lesson.number) && Objects.equals(startTime, lesson.startTime) && Objects.equals(duration, lesson.duration) && Objects.equals(subjectId, lesson.subjectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, startTime, duration, subjectId);
    }

    @Override
    public String
    toString() {
        return "Lesson{" +
                "id=" + id +
                ", number=" + number +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", subjectId=" + subjectId +
                '}';
    }
}
