package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.repository.LessonRepository;
import com.foxminded.university.management.schedule.repository.SubjectRepository;
import com.foxminded.university.management.schedule.service.LessonService;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LessonServiceImpl implements LessonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonServiceImpl.class);

    private final LessonRepository lessonRepository;
    private final SubjectRepository subjectRepository;

    public LessonServiceImpl(LessonRepository lessonRepository, SubjectRepository subjectRepository) {
        this.lessonRepository = lessonRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    public Lesson saveLesson(Lesson lesson) {
        boolean isSubjectPresent = subjectRepository.findById(lesson.getSubject().getId()).isPresent();
        LOGGER.debug("Subject is present: {}", isSubjectPresent);
        if (!isSubjectPresent)
            throw new EntityNotFoundException("Lesson subject with id: " + lesson.getSubject().getId() + " is not exist");
        return lessonRepository.save(lesson);
    }

    @Override
    public Lesson getLessonById(Long id) {
        boolean isLessonPresent = lessonRepository.findById(id).isPresent();
        LOGGER.debug("Lesson is present: {}", isLessonPresent);
        if (isLessonPresent) {
            return lessonRepository.findById(id).get();
        }
        throw new EntityNotFoundException("Lesson with id: " + id + " is not found");
    }

    @Override
    public List<Lesson> getAllLessons() {
        return lessonRepository.findAll();
    }

    @Override
    public void deleteLessonById(Long id) {
        boolean isLessonPresent = lessonRepository.findById(id).isPresent();
        LOGGER.debug("Lesson is present: {}", isLessonPresent);
        if (isLessonPresent) {
            lessonRepository.deleteById(getLessonById(id).getId());
        } else {
            throw new EntityNotFoundException("Lesson with id: " + id + " is not found");
        }
    }

    @Override
    public List<Lesson> saveAllLessons(List<Lesson> lessons) {
        List<Lesson> result = new ArrayList<>();
        lessons.forEach(lecture -> result.add(saveLesson(lecture)));
        return result;
    }

    @Override
    public List<Time> getStartTimesWithPossibleNullForLessons(List<Lesson> lessons) {
        LOGGER.debug("Getting start times for lessons {}", lessons);
        List<Time> result = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson == null) {
                result.add(null);
            } else {
                result.add(lesson.getStartTime());
            }
        }
        LOGGER.info("Start times for lessons {} received successful", lessons);
        return result;
    }

    @Override
    public List<Duration> getDurationsWithPossibleNullForLessons(List<Lesson> lessons) {
        LOGGER.debug("Getting durations for lessons {}", lessons);
        List<Duration> result = new ArrayList<>();
        for (Lesson lesson : lessons) {
            if (lesson == null) {
                result.add(null);
            } else {
                result.add(lesson.getDuration());
            }
        }
        LOGGER.info("Durations for lessons {} received successful", lessons);
        return result;
    }

    @Override
    public List<Lesson> getLessonsWithPossibleNullForLectures(List<Lecture> lectures) {
        LOGGER.debug("Getting lessons times for lectures {}", lectures);
        List<Lesson> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if (lecture.getLesson() == null) {
                result.add(null);
            } else {
                result.add(lecture.getLesson());
            }
        }
        LOGGER.info("Lessons for lectures {} received successful", lectures);
        return result;
    }

    public boolean isLessonWithIdExist(Long id) {
        return lessonRepository.findById(id).isPresent();
    }
}
