package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.exceptions.LessonServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LessonServiceImpl implements LessonService {
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private SubjectDao subjectDao;

    @Override
    public Lesson saveLesson(Lesson lesson) {
        return lessonDao.save(lesson);
    }

    @Override
    public Lesson getLessonById(Long id) {
        if (lessonDao.getById(id).isPresent()) {
            return lessonDao.getById(id).get();
        }
        throw new LessonServiceException("Lesson with id: " + id + " is not found");
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
            throw new LessonServiceException("Impossible to add subject to lesson. Subject with id: " + subject.getId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        if (!isLessonPresent)
            throw new LessonServiceException("Impossible to add subject to lesson. Lesson with id: " + lesson.getId() + " is not exist");

        if (lesson.getSubjectId() != null && lesson.getSubjectId().equals(subject.getId()))
            throw new LessonServiceException("Subject with id: " + subject.getId() + " is already added to lesson with id: " + lesson.getId());

        lesson.setSubjectId(subject.getId());
        return saveLesson(lesson);
    }

    @Override
    public Lesson removeSubjectFromLesson(Subject subject, Lesson lesson) {
        boolean isSubjectPresent = subjectDao.getById(subject.getId()).isPresent();
        if (!isSubjectPresent)
            throw new LessonServiceException("Impossible to remove subject from lesson. Subject with id: " + subject.getId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        if (!isLessonPresent)
            throw new LessonServiceException("Impossible to remove subject from lesson. Lesson with id: " + lesson.getId() + " is not exist");

        if (lesson.getSubjectId() == null)
            throw new LessonServiceException("Subject with id: " + subject.getId() + " is already removed from lesson with id: " + lesson.getId());

        lesson.setSubjectId(null);
        return saveLesson(lesson);
    }
}
