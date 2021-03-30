package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.dao.LessonDao;
import com.foxminded.university.management.schedule.dao.TeacherDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.LectureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LectureServiceImpl implements LectureService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LectureServiceImpl.class);
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
        boolean isTeacherPresent = teacherDao.getById(lecture.getTeacher().getId()).isPresent();
        LOGGER.debug("Teacher is present: {}", isTeacherPresent);
        if (!isTeacherPresent)
            throw new ServiceException("Lecture teacher with id: " + lecture.getTeacher().getId() + " is not exist");

        boolean isAudiencePresent = audienceDao.getById(lecture.getAudience().getId()).isPresent();
        LOGGER.debug("Audience is present: {}", isAudiencePresent);
        if (!isAudiencePresent)
            throw new ServiceException("Lecture audience with id: " + lecture.getAudience().getId() + " is not exist");

        boolean isLessonPresent = lessonDao.getById(lecture.getLesson().getId()).isPresent();
        LOGGER.debug("Audience is present: {}", isLessonPresent);
        if (!isLessonPresent)
            throw new ServiceException("Lecture lesson with id: " + lecture.getLesson().getId() + " is not exist");

        return lectureDao.save(lecture);
    }

    @Override
    public Lecture getLectureById(Long id) {
        boolean isLecturePresent = lectureDao.getById(id).isPresent();
        LOGGER.debug("Lecture is present: {}", isLecturePresent);
        if (isLecturePresent) {
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
}
