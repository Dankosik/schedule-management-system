package com.foxminded.university.management.schedule.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foxminded.university.management.schedule.models.validators.CurrentYear;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "lectures")
public class Lecture {
    @Schema(description = "Unique identifier of the Lecture.",
            example = "1", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Number of the lecture.",
            example = "2", required = true)
    private Integer number;
    @Schema(description = "Date of the lecture.",
            example = "2021-01-03", required = true)
    @CurrentYear
    private Date date;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "audience_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Audience audience;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "group_id")
    private Group group;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    public Lecture() {

    }

    public Lecture(Long id, Integer number, Date date, Audience audience, Group group, Lesson lesson, Teacher teacher) {
        this.id = id;
        this.number = number;
        this.date = date;
        this.audience = audience;
        this.group = group;
        this.lesson = lesson;
        this.teacher = teacher;
    }

    public Lecture(Integer number, Date date, Audience audience, Group group, Lesson lesson, Teacher teacher) {
        this.number = number;
        this.date = date;
        this.audience = audience;
        this.group = group;
        this.lesson = lesson;
        this.teacher = teacher;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(number, lecture.number) && Objects.equals(date, lecture.date) &&
                Objects.equals(audience, lecture.audience) && Objects.equals(group, lecture.group) &&
                Objects.equals(lesson, lecture.lesson) && Objects.equals(teacher, lecture.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, date, audience, group, lesson, teacher);
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", number=" + number +
                ", date=" + date +
                ", audience=" + audience +
                ", group=" + group +
                ", lesson=" + lesson +
                ", teacher=" + teacher +
                '}';
    }
}
