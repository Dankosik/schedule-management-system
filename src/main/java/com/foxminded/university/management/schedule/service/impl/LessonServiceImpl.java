package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.LessonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {
    private final LessonDao lessonDao;
    private final SubjectDao subjectDao;

    public LessonServiceImpl(LessonDao lessonDao, SubjectDao subjectDao) {
        this.lessonDao = lessonDao;
        this.subjectDao = subjectDao;
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        boolean isSubjectPresent = subjectDao.getById(lesson.getSubjectId()).isPresent();
        if (!isSubjectPresent)
            throw new ServiceException("Lesson subject with id: " + lesson.getSubjectId() + " is not exist");
        return lessonDao.save(lesson);
    }

    @Override
    public Lesson getLessonById(Long id) {
        if (lessonDao.getById(id).isPresent()) {
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
        boolean isSubjectPresent = subjectDao.getById(subject.getId()).isPresent();
        if (!isSubjectPresent)
            throw new ServiceException("Impossible to add subject to lesson. Subject with id: " + subject.getId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        if (!isLessonPresent)
            throw new ServiceException("Impossible to add subject to lesson. Lesson with id: " + lesson.getId() + " is not exist");

        if (lesson.getSubjectId() != null && lesson.getSubjectId().equals(subject.getId()))
            throw new ServiceException("Subject with id: " + subject.getId() + " is already added to lesson with id: " + lesson.getId());

        lesson.setSubjectId(subject.getId());
        return saveLesson(lesson);
    }

    @Override
    public Lesson removeSubjectFromLesson(Subject subject, Lesson lesson) {
        boolean isSubjectPresent = subjectDao.getById(subject.getId()).isPresent();
        if (!isSubjectPresent)
            throw new ServiceException("Impossible to remove subject from lesson. Subject with id: " + subject.getId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        if (!isLessonPresent)
            throw new ServiceException("Impossible to remove subject from lesson. Lesson with id: " + lesson.getId() + " is not exist");

        if (lesson.getSubjectId() == null)
            throw new ServiceException("Subject with id: " + subject.getId() + " is already removed from lesson with id: " + lesson.getId());

        lesson.setSubjectId(null);
        return saveLesson(lesson);
    }
}