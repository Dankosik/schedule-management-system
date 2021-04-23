package com.foxminded.university.management.schedule.dto.lecture;

import com.foxminded.university.management.schedule.dto.audience.AudienceUpdateDto;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonUpdateDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherUpdateDto;

import java.sql.Date;

public interface LectureDto {
    Integer getNumber();

    void setNumber(Integer number);

    Date getDate();

    void setDate(Date date);

    AudienceUpdateDto getAudience();

    void setAudience(AudienceUpdateDto audience);

    GroupUpdateDto getGroup();

    void setGroup(GroupUpdateDto group);

    LessonUpdateDto getLesson();

    void setLesson(LessonUpdateDto lesson);

    TeacherUpdateDto getTeacher();

    void setTeacher(TeacherUpdateDto teacher);
}
