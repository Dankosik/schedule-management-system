package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.LectureService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LectureServiceImpl implements LectureService {
    private final LectureDao lectureDao;
    private final LessonDao lessonDao;
    private final TeacherDao teacherDao;
    private final AudienceDao audienceDao;

    public LectureServiceImpl(LectureDao lectureDao, LessonDao lessonDao, TeacherDao teacherDao, AudienceDao audienceDao) {
        this.lectureDao = lectureDao;
        this.lessonDao = lessonDao;
        this.teacherDao = teacherDao;
        this.audienceDao = audienceDao;
    }

    @Override
    public Lecture saveLecture(Lecture lecture) {
        boolean isTeacherPresent = teacherDao.getById(lecture.getTeacherId()).isPresent();
        if (!isTeacherPresent)
            throw new ServiceException("Lecture teacher with id: " + lecture.getTeacherId() + " is not exist");

        boolean isAudiencePresent = audienceDao.getById(lecture.getAudienceId()).isPresent();
        if (!isAudiencePresent)
            throw new ServiceException("Lecture audience with id: " + lecture.getAudienceId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lecture.getLessonId()).isPresent();
        if (!isLessonPresent)
            throw new ServiceException("Lecture lesson with id: " + lecture.getLessonId() + " is not exist");

        return lectureDao.save(lecture);
    }

    @Override
    public Lecture getLectureById(Long id) {
        if (lectureDao.getById(id).isPresent()) {
            return lectureDao.getById(id).get();
        }
        throw new ServiceException("Lecture with id: " + id + " is not found");
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
        if (!isLessonPresent)
            throw new ServiceException("Impossible to add lesson to lecture. Lesson with id: " + lesson.getId() + " is not exist");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new ServiceException("Impossible to add lesson to lecture. Lecture with id: " + lecture.getId() + " is not exist");

        if (lecture.getLessonId() != null && lecture.getLessonId().equals(lesson.getId()))
            throw new ServiceException("Lesson with id: " + lesson.getId() + " is already added to lecture with id: " + lecture.getId());

        lecture.setLessonId(lesson.getId());
        return saveLecture(lecture);
    }

    @Override
    public Lecture removeLessonFromLecture(Lesson lesson, Lecture lecture) {
        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        if (!isLessonPresent)
            throw new ServiceException("Impossible to remove lesson from lecture. Lesson with id: " + lesson.getId() + " is not exist");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new ServiceException("Impossible to remove lesson from lecture. Lecture with id: " + lecture.getId() + " is not exist");

        if (lecture.getLessonId() == null)
            throw new ServiceException("Lesson with id: " + lesson.getId() + " is already removed from lecture with id: " + lecture.getId());

        lecture.setLessonId(null);
        return saveLecture(lecture);
    }

    @Override
    public Lecture addTeacherToLecture(Teacher teacher, Lecture lecture) {
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        if (!isTeacherPresent)
            throw new ServiceException("Impossible to add teacher to lecture. Teacher with id: " + teacher.getId() + " not exist");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new ServiceException("Impossible to add teacher to lecture. Lecture with id: " + lecture.getId() + " not exist");

        if (lecture.getTeacherId() != null && lecture.getTeacherId().equals(teacher.getId()))
            throw new ServiceException("Teacher with id: " + teacher.getId() + " is already added to lecture with id: " + lecture.getId());

        lecture.setTeacherId(teacher.getId());
        return saveLecture(lecture);
    }

    @Override
    public Lecture removeTeacherFromLecture(Teacher teacher, Lecture lecture) {
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        if (!isTeacherPresent)
            throw new ServiceException("Impossible to remove teacher from lecture. Teacher with id: " + teacher.getId() + " not exist");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new ServiceException("Impossible to remove teacher from lecture. Lecture with id: " + lecture.getId() + " not exist");

        if (lecture.getTeacherId() == null)
            throw new ServiceException("Teacher with id: " + teacher.getId() + " is already removed from lecture with id: " + lecture.getId());

        lecture.setTeacherId(null);
        return saveLecture(lecture);
    }
}
