package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.SubjectDao;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.exceptions.FacultyServiceException;
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
    public Lesson getAudienceById(Long id) {
        if (lessonDao.getById(id).isPresent()) {
            return lessonDao.getById(id).get();
        }
        throw new LessonServiceException("Lesson with id: " + id + " not found");
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonDao.getAll();
    }

    @Override
    public void deleteLessonById(Long id) {
        lessonDao.deleteById(getAudienceById(id).getId());
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
        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        if (isSubjectPresent && isLessonPresent) {
            lesson.setSubjectId(subject.getId());
            return saveLesson(lesson);
        }
        if (!isSubjectPresent)
            throw new FacultyServiceException("Cant add subject to lesson cause subject with id: " + subject.getId() + " not exist");
        throw new FacultyServiceException("Cant add subject to lesson cause lesson with id: " + lesson.getId() + " not exist");
    }

    @Override
    public Lesson removeSubjectFromLesson(Subject subject, Lesson lesson) {
        boolean isSubjectPresent = subjectDao.getById(subject.getId()).isPresent();
        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        if (isSubjectPresent && isLessonPresent) {
            lesson.setSubjectId(null);
            return saveLesson(lesson);
        }
        if (!isSubjectPresent)
            throw new FacultyServiceException("Cant remove subject from lesson cause subject with id: " + subject.getId() + " not exist");
        throw new FacultyServiceException("Cant remove subject from lesson cause lesson with id: " + lesson.getId() + " not exist");
    }
}
