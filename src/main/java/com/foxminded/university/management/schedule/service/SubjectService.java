package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectService {
    Subject saveSubject(Subject subject);

    Optional<Subject> getSubjectById(Long id);

    List<Subject> getAllSubjects();

    boolean deleteSubjectById(Long id);

    List<Subject> saveAllSubjects(List<Subject> subjects);
}
