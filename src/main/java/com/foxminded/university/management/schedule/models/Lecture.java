package com.foxminded.university.management.schedule.models;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Lecture {
    private Integer number;
    private Audience audience;
    private List<Group> groups;
    private Teacher teacher;
    private LocalDate date;
    private Lesson lesson;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Lesson getLesson() {
        return lesson;
    }

    public void setLesson(Lesson lesson) {
        this.lesson = lesson;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(number, lecture.number) && Objects.equals(audience, lecture.audience) &&
                Objects.equals(groups, lecture.groups) && Objects.equals(teacher, lecture.teacher) &&
                Objects.equals(date, lecture.date) && Objects.equals(lesson, lecture.lesson);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, audience, groups, teacher, date, lesson);
    }
}
