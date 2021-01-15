package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;

import java.util.List;

public interface LessonService {
    Lesson saveLesson(Lesson lesson);

    Lesson getAudienceById(Long id);

    List<Lesson> getAllLessons();

    void deleteLessonById(Long id);

    List<Lesson> saveAllLessons(List<Lesson> lessons);

    Lesson addSubjectToLesson(Subject subject, Lesson lesson);

    Lesson removeSubjectFromLesson(Subject subject, Lesson lesson);
}
