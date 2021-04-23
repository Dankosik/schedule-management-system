package com.foxminded.university.management.schedule.dto.lesson;

import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;

import java.sql.Time;
import java.time.Duration;

public interface LessonDto {
    Integer getNumber();

    void setNumber(Integer number);

    Time getStartTime();

    void setStartTime(Time startTime);

    Duration getDuration();

    void setDuration(Duration duration);

    SubjectUpdateDto getSubject();

    void setSubject(SubjectUpdateDto subject);
}
