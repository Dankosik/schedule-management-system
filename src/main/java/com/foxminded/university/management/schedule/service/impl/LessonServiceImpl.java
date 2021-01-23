package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.LessonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonServiceImpl.class);

    private final LessonDao lessonDao;
    private final SubjectDao subjectDao;

    public LessonServiceImpl(LessonDao lessonDao, SubjectDao subjectDao) {
        this.lessonDao = lessonDao;
        this.subjectDao = subjectDao;
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        boolean isSubjectPresent = subjectDao.getById(lesson.getSubjectId()).isPresent();
        LOGGER.debug("Subject is present: {}", isSubjectPresent);
        if (!isSubjectPresent)
            throw new ServiceException("Lesson subject with id: " + lesson.getSubjectId() + " is not exist");
        return lessonDao.save(lesson);
    }

    @Override
    public Lesson getLessonById(Long id) {
        boolean isLessonPresent = lessonDao.getById(id).isPresent();
        LOGGER.debug("Lesson is present: {}", isLessonPresent);
        if (isLessonPresent) {
            return lessonDao.getById(id).get();
        }
        throw new ServiceException("Lesson with id: " + id + " is not found");
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonDao.getAll();
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonDao.deleteById(getLessonById(id).getId());
    }

    @Override
    public List<Lesson> saveAllLessons(List<Lesson> lessons) {
        List<Lesson> result = new ArrayList<>();
        lessons.forEach(lecture -> result.add(saveLesson(lecture)));
        return result;
    }

    @Override
    public Lesson addSubjectToLesson(Subject subject, Lesson lesson) {
        LOGGER.debug("Adding subject {} to lesson {}", subject, lesson);
        boolean isSubjectPresent = subjectDao.getById(subject.getId()).isPresent();
        LOGGER.debug("Subject is present: {}", isSubjectPresent);
        if (!isSubjectPresent)
            throw new ServiceException("Impossible to add subject to lesson. Subject with id: " + subject.getId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        LOGGER.debug("Lesson is present: {}", isLessonPresent);
        if (!isLessonPresent)
            throw new ServiceException("Impossible to add subject to lesson. Lesson with id: " + lesson.getId() + " is not exist");

        boolean isSubjectAlreadyAddedToLesson = lesson.getSubjectId() != null && lesson.getSubjectId().equals(subject.getId());
        LOGGER.debug("Subject is already added to lesson: {}", isSubjectAlreadyAddedToLesson);
        if (isSubjectAlreadyAddedToLesson)
            throw new ServiceException("Subject with id: " + subject.getId() + " is already added to lesson with id: " + lesson.getId());

        lesson.setSubjectId(subject.getId());
        Lesson result = saveLesson(lesson);
        LOGGER.info("Successful adding subject {} to lesson {}", subject, lesson);
        return result;
    }

    @Override
    public Lesson removeSubjectFromLesson(Subject subject, Lesson lesson) {
        LOGGER.debug("Removing subject {} from lesson {}", subject, lesson);
        boolean isSubjectPresent = subjectDao.getById(subject.getId()).isPresent();
        LOGGER.debug("Subject is present: {}", isSubjectPresent);
        if (!isSubjectPresent)
            throw new ServiceException("Impossible to remove subject from lesson. Subject with id: " + subject.getId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        LOGGER.debug("Lesson is present: {}", isLessonPresent);
        if (!isLessonPresent)
            throw new ServiceException("Impossible to remove subject from lesson. Lesson with id: " + lesson.getId() + " is not exist");

        boolean isSubjectAlreadyRemovedFromLesson = lesson.getSubjectId() == null;
        LOGGER.debug("Subject is already removed from lesson: {}", isSubjectAlreadyRemovedFromLesson);
        if (isSubjectAlreadyRemovedFromLesson)
            throw new ServiceException("Subject with id: " + subject.getId() + " is already removed from lesson with id: " + lesson.getId());

        lesson.setSubjectId(null);
        Lesson result = saveLesson(lesson);
        LOGGER.info("Successful removing subject {} from lesson {}", subject, lesson);
        return result;
    }
}
