package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;

import java.util.List;

public interface SubjectService {
    Subject saveSubject(Subject subject);

    Subject getSubjectById(Long id);

    List<Subject> getAllSubjects();

    void deleteSubjectById(Long id);

    List<Subject> saveAllSubjects(List<Subject> subjects);

    List<String> getSubjectNamesForLessons(List<Lesson> lessons);

    List<Subject> getSubjectsForLectures(List<Lecture> lectures);

    List<Subject> getSubjectsWithPossibleNullForLessons(List<Lesson> lessons);

    boolean isSubjectWithIdExist(Long id);
}
