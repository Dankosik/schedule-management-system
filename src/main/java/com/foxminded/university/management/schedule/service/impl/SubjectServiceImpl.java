package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectServiceImpl.class);
    private final SubjectDao subjectDao;
    private final LessonServiceImpl lessonService;

    public SubjectServiceImpl(SubjectDao subjectDao, LessonServiceImpl lessonService) {
        this.subjectDao = subjectDao;
        this.lessonService = lessonService;
    }

    @Override
    public Subject saveSubject(Subject subject) {
        try {
            return subjectDao.save(subject);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("Subject with name: " + subject.getName() + "is already exist");
        }
    }

    @Override
    public Subject getSubjectById(Long id) {
        boolean isSubjectPresent = subjectDao.getById(id).isPresent();
        LOGGER.debug("Audience is present: {}", isSubjectPresent);
        if (isSubjectPresent) {
            return subjectDao.getById(id).get();
        }
        throw new ServiceException("Subject with id: " + id + " is not found");
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectDao.getAll();
    }

    @Override
    public void deleteSubjectById(Long id) {
        subjectDao.deleteById(getSubjectById(id).getId());
    }

    @Override
    public List<Subject> saveAllSubjects(List<Subject> subjects) {
        List<Subject> result = new ArrayList<>();
        subjects.forEach(subject -> result.add(saveSubject(subject)));
        return result;
    }

    @Override
    public List<String> getSubjectNamesForLessons(List<Lesson> lessons) {
        LOGGER.debug("Getting subject names for lessons {}", lessons);
        List<String> result = new ArrayList<>();
        lessons.forEach(lesson -> result.add(getSubjectById(lesson.getSubjectId()).getName()));
        LOGGER.info("Subject names for lessons {} received successful", lessons);
        return result;
    }

    @Override
    public List<Subject> getSubjectsForLectures(List<Lecture> lectures) {
        LOGGER.debug("Getting subjects for lectures {}", lectures);
        List<Subject> subjects = lectures.stream()
                .map(lecture -> lessonService.getLessonById(lecture.getLessonId()).getSubjectId())
                .map(this::getSubjectById)
                .collect(Collectors.toList());
        LOGGER.info("Subject for lectures {} received successful", lectures);
        return subjects;
    }

    @Override
    public List<Subject> getSubjectsForLessons(List<Lesson> lessons) {
        LOGGER.debug("Getting subjects for lessons {}", lessons);
        List<Subject> subjects = lessons.stream()
                .map(Lesson::getSubjectId)
                .map(this::getSubjectById)
                .collect(Collectors.toList());
        LOGGER.info("Subject for lessons {} received successful", lessons);
        return subjects;
    }
}
