package com.foxminded.university.management.schedule.models;

import java.time.LocalDate;
import java.util.Objects;

public class Lecture {
    private Long id;
    private Integer number;
    private Long audienceId;
    private Long lessonId;
    private Long teacherId;
    private Long scheduleId;
    private LocalDate date;

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

    public Long getAudienceId() {
        return audienceId;
    }

    public void setAudienceId(Long audienceId) {
        this.audienceId = audienceId;
    }

    public Long getLessonId() {
        return lessonId;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.id) && Objects.equals(number, lecture.number) &&
                Objects.equals(audienceId, lecture.audienceId) && Objects.equals(lessonId, lecture.lessonId) &&
                Objects.equals(teacherId, lecture.teacherId) && Objects.equals(scheduleId, lecture.scheduleId) && Objects.equals(date, lecture.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, audienceId, lessonId, teacherId, scheduleId, date);
    }
}
