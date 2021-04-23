package com.foxminded.university.management.schedule.dto.lecture;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.dto.audience.AudienceUpdateDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonUpdateDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherUpdateDto;
import com.foxminded.university.management.schedule.models.validators.CurrentYear;

import javax.validation.constraints.NotNull;
import java.sql.Date;

public class LectureUpdateDto implements LectureDto {
    @NotNull
    private Long id;
    private Integer number;
    @CurrentYear
    private Date date;
    @NotNull
    private AudienceUpdateDto audience;
    @NotNull
    private GroupUpdateDto group;
    @NotNull
    private LessonUpdateDto lesson;
    @NotNull
    private TeacherUpdateDto teacher;

    @JsonCreator
    public LectureUpdateDto(@JsonProperty(required = true) Long id, @JsonProperty(required = true) Integer number,
                            @JsonProperty(required = true) Date date, @JsonProperty(required = true) AudienceUpdateDto audience,
                            @JsonProperty(required = true) GroupUpdateDto group, @JsonProperty(required = true) LessonUpdateDto lesson,
                            @JsonProperty(required = true) TeacherUpdateDto teacher) {
        this.id = id;
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

    @Override
    public Integer getNumber() {
        return number;
    }

    @Override
    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public AudienceUpdateDto getAudience() {
        return audience;
    }

    @Override
    public void setAudience(AudienceUpdateDto audience) {
        this.audience = audience;
    }

    @Override
    public GroupUpdateDto getGroup() {
        return group;
    }

    @Override
    public void setGroup(GroupUpdateDto group) {
        this.group = group;
    }

    @Override
    public LessonUpdateDto getLesson() {
        return lesson;
    }

    @Override
    public void setLesson(LessonUpdateDto lesson) {
        this.lesson = lesson;
    }

    @Override
    public TeacherUpdateDto getTeacher() {
        return teacher;
    }

    @Override
    public void setTeacher(TeacherUpdateDto teacher) {
        this.teacher = teacher;
    }
}
