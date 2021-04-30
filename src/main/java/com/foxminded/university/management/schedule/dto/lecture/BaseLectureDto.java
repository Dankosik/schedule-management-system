package com.foxminded.university.management.schedule.dto.lecture;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Date;

public class BaseLectureDto {
    @Schema(description = "Unique identifier of the Lecture.",
            example = "1", required = true)
    private Long id;
    @Schema(description = "Number of the lecture.",
            example = "2", required = true)
    private Integer number;
    @Schema(description = "Date of the lecture.",
            example = "2021-01-03", required = true)
    private Date date;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Audience audience;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Group group;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Lesson lesson;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Teacher teacher;

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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}
