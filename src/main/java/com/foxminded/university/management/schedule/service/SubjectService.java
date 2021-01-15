package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Subject;

import java.util.List;

public interface SubjectService {
    Subject saveSubject(Subject subject);

    Subject getSubjectById(Long id);

    List<Subject> getAllSubjects();

    void deleteSubjectById(Long id);

    List<Subject> saveAllSubjects(List<Subject> subjects);
}
