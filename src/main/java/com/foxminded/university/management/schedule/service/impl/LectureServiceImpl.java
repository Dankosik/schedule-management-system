package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.repository.AudienceRepository;
import com.foxminded.university.management.schedule.repository.LectureRepository;
import com.foxminded.university.management.schedule.repository.LessonRepository;
import com.foxminded.university.management.schedule.repository.TeacherRepository;
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
    private final LectureRepository lectureRepository;
    private final LessonRepository lessonRepository;
    private final TeacherRepository teacherRepository;
    private final AudienceRepository audienceRepository;

    public LectureServiceImpl(LectureRepository lectureRepository, LessonRepository lessonRepository,
                              TeacherRepository teacherRepository, AudienceRepository audienceRepository) {
        this.lectureRepository = lectureRepository;
        this.lessonRepository = lessonRepository;
        this.teacherRepository = teacherRepository;
        this.audienceRepository = audienceRepository;
    }


    @Override
    public Lecture saveLecture(Lecture lecture) {
        boolean isTeacherPresent = teacherRepository.findById(lecture.getTeacher().getId()).isPresent();
        LOGGER.debug("Teacher is present: {}", isTeacherPresent);
        if (!isTeacherPresent)
            throw new ServiceException("Lecture teacher with id: " + lecture.getTeacher().getId() + " is not exist");

        boolean isAudiencePresent = audienceRepository.findById(lecture.getAudience().getId()).isPresent();
        LOGGER.debug("Audience is present: {}", isAudiencePresent);
        if (!isAudiencePresent)
            throw new ServiceException("Lecture audience with id: " + lecture.getAudience().getId() + " is not exist");

        boolean isLessonPresent = lessonRepository.findById(lecture.getLesson().getId()).isPresent();
        LOGGER.debug("Audience is present: {}", isLessonPresent);
        if (!isLessonPresent)
            throw new ServiceException("Lecture lesson with id: " + lecture.getLesson().getId() + " is not exist");

        return lectureRepository.save(lecture);
    }

    @Override
    public Lecture getLectureById(Long id) {
        boolean isLecturePresent = lectureRepository.findById(id).isPresent();
        LOGGER.debug("Lecture is present: {}", isLecturePresent);
        if (isLecturePresent) {
            return lectureRepository.findById(id).get();
        }
        throw new ServiceException("Lecture with id: " + id + " is not found");
    }

    @Override
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
    }

    @Override
    public void deleteLectureById(Long id) {
        lectureRepository.deleteById(getLectureById(id).getId());
    }

    @Override
    public List<Lecture> saveAllLectures(List<Lecture> lectures) {
        List<Lecture> result = new ArrayList<>();
        lectures.forEach(lecture -> result.add(saveLecture(lecture)));
        return result;
    }

    @Override
    public boolean isLectureWithIdExist(Long id) {
        return lectureRepository.findById(id).isPresent();
    }
}
