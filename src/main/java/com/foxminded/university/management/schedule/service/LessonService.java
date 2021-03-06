package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;

import java.sql.Time;
import java.time.Duration;
import java.util.List;

public interface LessonService {
    Lesson saveLesson(Lesson lesson);

    Lesson getLessonById(Long id);

    List<Lesson> getAllLessons();

    void deleteLessonById(Long id);

    List<Lesson> saveAllLessons(List<Lesson> lessons);

    List<Duration> getDurationsWithPossibleNullForLessons(List<Lesson> lessons);

    List<Time> getStartTimesWithPossibleNullForLessons(List<Lesson> lessons);

    List<Lesson> getLessonsWithPossibleNullForLectures(List<Lecture> lectures);

    boolean isLessonWithIdExist(Long id);
}
