package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.exceptions.LectureServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LectureServiceImpl implements LectureService {
    @Autowired
    private LectureDao lectureDao;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private TeacherDao teacherDao;

    @Override
    public Lecture saveLecture(Lecture lecture) {
        return lectureDao.save(lecture);
    }

    @Override
    public Lecture getLectureById(Long id) {
        if (lectureDao.getById(id).isPresent()) {
            return lectureDao.getById(id).get();
        }
        throw new LectureServiceException("Lecture with id: " + id + "is not found");
    }

    @Override
    public List<Lecture> getAllLectures() {
        return lectureDao.getAll();
    }

    @Override
    public void deleteLectureById(Long id) {
        lectureDao.deleteById(getLectureById(id).getId());
    }

    @Override
    public List<Lecture> saveAllLectures(List<Lecture> lectures) {
        List<Lecture> result = new ArrayList<>();
        lectures.forEach(lecture -> result.add(saveLecture(lecture)));
        return result;
    }

    @Override
    public Lecture addLessonToLecture(Lesson lesson, Lecture lecture) {
        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (isLessonPresent && isLecturePresent) {
            lecture.setLessonId(lesson.getId());
            return saveLecture(lecture);
        }
        if (!isLessonPresent)
            throw new LectureServiceException("Impossible to add lesson to lecture. Lesson with id: " + lesson.getId() + "is not exist");
        throw new LectureServiceException("Impossible to add lesson to lecture. Lecture with id: " + lecture.getId() + "is not exist");
    }

    @Override
    public Lecture removeLessonFromLecture(Lesson lesson, Lecture lecture) {
        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (isLessonPresent && isLecturePresent) {
            lecture.setLessonId(null);
            return saveLecture(lecture);
        }
        if (!isLessonPresent)
            throw new LectureServiceException("Impossible to remove lesson from lecture. Lesson with id: " + lesson.getId() + "is not exist");
        throw new LectureServiceException("Impossible to remove lesson from lecture. Lecture with id: " + lecture.getId() + "is not exist");
    }

    @Override
    public Lecture addTeacherToLecture(Teacher teacher, Lecture lecture) {
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (isTeacherPresent && isLecturePresent) {
            lecture.setTeacherId(teacher.getId());
            return saveLecture(lecture);
        }
        if (!isTeacherPresent)
            throw new LectureServiceException("Cant add teacher to lecture. Teacher with id: " + teacher.getId() + " not exist");
        throw new LectureServiceException("Cant add teacher to lecture. Lecture with id: " + lecture.getId() + " not exist");
    }

    @Override
    public Lecture removeTeacherFromLecture(Teacher teacher, Lecture lecture) {
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (isTeacherPresent && isLecturePresent) {
            lecture.setTeacherId(null);
            return saveLecture(lecture);
        }
        if (!isTeacherPresent)
            throw new LectureServiceException("Cant remove teacher from lecture. Teacher with id: " + teacher.getId() + " not exist");
        throw new LectureServiceException("Cant remove teacher from lecture. Lecture with id: " + lecture.getId() + " not exist");
    }
}
