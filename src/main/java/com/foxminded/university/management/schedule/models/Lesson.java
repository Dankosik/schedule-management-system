package com.foxminded.university.management.schedule.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foxminded.university.management.schedule.models.validators.DurationConstraint;
import com.foxminded.university.management.schedule.models.validators.StartTime;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Time;
import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Schema(description = "Unique identifier of the Lesson.",
            example = "1", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Number of the lesson.",
            example = "2", required = true)
    @NotNull
    @Min(value = 1, message = "Must be at least 1")
    @Max(value = 8, message = "Must be no more than 8")
    private Integer number;
    @Schema(description = "Start time of the lesson.",
            example = "13:30:00", required = true)
    @StartTime
    private Time startTime;
    @Schema(description = "Duration of the lesson.",
            example = "PT1H30M", required = true)
    @DurationConstraint
    private Duration duration;
    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;
    @JsonIgnore
    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    private List<Lecture> lectures;

    public Lesson() {

    }

    public Lesson(Long id, Integer number, Time startTime, Duration duration, Subject subject, List<Lecture> lectures) {
        this.id = id;
        this.number = number;
        this.startTime = startTime;
        this.duration = duration;
        this.subject = subject;
        this.lectures = lectures;
    }

    public Lesson(Integer number, Time startTime, Duration duration, Subject subject, List<Lecture> lectures) {
        this.number = number;
        this.startTime = startTime;
        this.duration = duration;
        this.subject = subject;
        this.lectures = lectures;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return Objects.equals(number, lesson.number) && Objects.equals(startTime, lesson.startTime) &&
                Objects.equals(duration, lesson.duration) && Objects.equals(subject, lesson.subject) &&
                Objects.equals(lectures, lesson.lectures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, startTime, duration, subject, lectures);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "id=" + id +
                ", number=" + number +
                ", startTime=" + startTime +
                ", duration=" + duration +
                ", subject=" + subject +
                '}';
    }
}
