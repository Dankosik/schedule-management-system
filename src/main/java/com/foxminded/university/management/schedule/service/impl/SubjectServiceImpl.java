package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.repository.SubjectRepository;
import com.foxminded.university.management.schedule.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectServiceImpl.class);
    private final SubjectRepository subjectRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Subject saveSubject(Subject subject) {
        try {
            return subjectRepository.saveAndFlush(subject);
        } catch (DataIntegrityViolationException e) {
            throw new ServiceException("Subject with name: " + subject.getName() + " is already exist");
        }
    }

    @Override
    public Subject getSubjectById(Long id) {
        boolean isSubjectPresent = subjectRepository.findById(id).isPresent();
        LOGGER.debug("Audience is present: {}", isSubjectPresent);
        if (isSubjectPresent) {
            return subjectRepository.findById(id).get();
        }
        throw new ServiceException("Subject with id: " + id + " is not found");
    }

    @Override
    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    @Override
    public void deleteSubjectById(Long id) {
        subjectRepository.deleteById(getSubjectById(id).getId());
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
        for (Lesson lesson : lessons) {
            if (lesson == null) {
                result.add(null);
                continue;
            }
            if (lesson.getSubject() == null) {
                result.add(null);
            } else {
                result.add(lesson.getSubject().getName());
            }
        }
        LOGGER.info("Subject names for lessons {} received successful", lessons);
        return result;
    }

    @Override
    public List<Subject> getSubjectsForLectures(List<Lecture> lectures) {
        LOGGER.debug("Getting subjects for lectures {}", lectures);
        List<Subject> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if (lecture.getLesson() == null) {
                result.add(null);
                continue;
            }
            if (lecture.getLesson().getSubject() == null) {
                result.add(null);
            } else {
                result.add(lecture.getLesson().getSubject());
            }
        }
        LOGGER.info("Subject for lectures {} received successful", lectures);
        return result;
    }

    @Override
    public List<Subject> getSubjectsWithPossibleNullForLessons(List<Lesson> lessons) {
        LOGGER.debug("Getting subjects for lessons {}", lessons);
        List<Subject> result = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson == null) {
                result.add(null);
                continue;
            }
            if (lesson.getId() == 0) {
                result.add(null);
                continue;
            }
            if (lesson.getSubject() == null) {
                result.add(null);
            } else {
                result.add(lesson.getSubject());
            }
        }
        LOGGER.info("Subject for lessons {} received successful", lessons);
        return result;
    }

    @Override
    public boolean isSubjectWithIdExist(Long id) {
        return subjectRepository.findById(id).isPresent();
    }
}
