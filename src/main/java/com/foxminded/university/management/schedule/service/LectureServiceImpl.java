package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.exceptions.LectureServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LectureServiceImpl implements LectureService {
    @Autowired
    private LectureDao lectureDao;
    @Autowired
    private LessonDao lessonDao;
    @Autowired
    private TeacherDao teacherDao;
    @Autowired
    private AudienceDao audienceDao;

    @Override
    public Lecture saveLecture(Lecture lecture) {
        boolean isTeacherPresent = teacherDao.getById(lecture.getTeacherId()).isPresent();
        if (!isTeacherPresent)
            throw new LectureServiceException("Lecture teacher with id: " + lecture.getTeacherId() + " is not exist");

        boolean isAudiencePresent = audienceDao.getById(lecture.getAudienceId()).isPresent();
        if (!isAudiencePresent)
            throw new LectureServiceException("Lecture audience with id: " + lecture.getAudienceId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lecture.getLessonId()).isPresent();
        if (!isLessonPresent)
            throw new LectureServiceException("Lecture lesson with id: " + lecture.getLessonId() + " is not exist");

        return lectureDao.save(lecture);
    }

    @Override
    public Lecture getLectureById(Long id) {
        if (lectureDao.getById(id).isPresent()) {
            return lectureDao.getById(id).get();
        }
        throw new LectureServiceException("Lecture with id: " + id + " is not found");
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
            throw new LectureServiceException("Impossible to add lesson to lecture. Lesson with id: " + lesson.getId() + " is not exist");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new LectureServiceException("Impossible to add lesson to lecture. Lecture with id: " + lecture.getId() + " is not exist");

        if (lecture.getLessonId() != null && lecture.getLessonId().equals(lesson.getId()))
            throw new LectureServiceException("Lesson with id: " + lesson.getId() + " is already added to lecture with id: " + lecture.getId());

        lecture.setLessonId(lesson.getId());
        return saveLecture(lecture);
    }

    @Override
    public Lecture removeLessonFromLecture(Lesson lesson, Lecture lecture) {
        boolean isLessonPresent = lessonDao.getById(lesson.getId()).isPresent();
        if (!isLessonPresent)
            throw new LectureServiceException("Impossible to remove lesson from lecture. Lesson with id: " + lesson.getId() + " is not exist");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new LectureServiceException("Impossible to remove lesson from lecture. Lecture with id: " + lecture.getId() + " is not exist");

        if (lecture.getLessonId() == null)
            throw new LectureServiceException("Lesson with id: " + lesson.getId() + " is already removed from lecture with id: " + lecture.getId());

        lecture.setLessonId(null);
        return saveLecture(lecture);
    }

    @Override
    public Lecture addTeacherToLecture(Teacher teacher, Lecture lecture) {
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        if (!isTeacherPresent)
            throw new LectureServiceException("Impossible to add teacher to lecture. Teacher with id: " + teacher.getId() + " not exist");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new LectureServiceException("Impossible to add teacher to lecture. Lecture with id: " + lecture.getId() + " not exist");

        if (lecture.getTeacherId() != null && lecture.getTeacherId().equals(teacher.getId()))
            throw new LectureServiceException("Teacher with id: " + teacher.getId() + " is already added to lecture with id: " + lecture.getId());

        lecture.setTeacherId(teacher.getId());
        return saveLecture(lecture);
    }

    @Override
    public Lecture removeTeacherFromLecture(Teacher teacher, Lecture lecture) {
        boolean isTeacherPresent = teacherDao.getById(teacher.getId()).isPresent();
        if (!isTeacherPresent)
            throw new LectureServiceException("Impossible to remove teacher from lecture. Teacher with id: " + teacher.getId() + " not exist");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new LectureServiceException("Impossible to remove teacher from lecture. Lecture with id: " + lecture.getId() + " not exist");

        if (lecture.getTeacherId() == null)
            throw new LectureServiceException("Teacher with id: " + teacher.getId() + " is already removed from lecture with id: " + lecture.getId());

        lecture.setTeacherId(null);
        return saveLecture(lecture);
    }
}
