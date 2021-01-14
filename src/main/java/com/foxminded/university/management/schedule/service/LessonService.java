package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;

import java.util.List;
import java.util.Optional;

public interface LessonService {
    Lesson saveLesson(Lesson lesson);

    Optional<Lesson> getAudienceById(Long id);

    List<Lesson> getAllLessons();

    boolean deleteLessonById(Long id);

    List<Lesson> saveAllLessons(List<Lesson> lessons);

    boolean addSubjectToLesson(Subject subject, Lesson lesson);

    boolean removeSubjectFromLesson(Subject subject, Lesson lesson);
}
