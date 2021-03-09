package com.foxminded.university.management.schedule.models;

import com.foxminded.university.management.schedule.dao.BaseEntity;

import java.sql.Date;
import java.util.Objects;

public class Lecture implements BaseEntity<Long> {
    private Long id;
    private Integer number;
    private Date date;
    private Long audienceId;
    private Long groupId;
    private Long lessonId;
    private Long teacherId;

    public Lecture() {

    }

    public Lecture(Long id, Integer number, Date date, Long audienceId, Long groupId, Long lessonId, Long teacherId) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.audienceId = audienceId;
        this.groupId = groupId;
        this.lessonId = lessonId;
        this.teacherId = teacherId;
    }

    public Lecture(Integer number, Date date, Long audienceId, Long groupId, Long lessonId, Long teacherId) {
        this.number = number;
        this.date = date;
        this.audienceId = audienceId;
        this.groupId = groupId;
        this.lessonId = lessonId;
        this.teacherId = teacherId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(number, lecture.number) && Objects.equals(date, lecture.date) &&
                Objects.equals(audienceId, lecture.audienceId) && Objects.equals(lessonId, lecture.lessonId)
                && Objects.equals(teacherId, lecture.teacherId) && Objects.equals(groupId, lecture.groupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, date, audienceId, groupId, lessonId, teacherId);
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", number=" + number +
                ", date=" + date +
                ", audienceId=" + audienceId +
                ", groupId=" + groupId +
                ", lessonId=" + lessonId +
                ", teacherId=" + teacherId +
                '}';
    }
}
