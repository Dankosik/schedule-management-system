package com.foxminded.schedule_managment_system.models;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public class Lesson {
    private Integer number;
    private LocalTime startTime;
    private Duration duration;
    private Subject subject;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(number, lesson.number) && Objects.equals(startTime, lesson.startTime) &&
                Objects.equals(duration, lesson.duration) && Objects.equals(subject, lesson.subject);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, startTime, duration, subject);
    }
}
