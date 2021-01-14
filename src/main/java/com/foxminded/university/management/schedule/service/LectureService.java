package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;

import java.util.List;
import java.util.Optional;

public interface LectureService {
    Lecture saveLecture(Lecture lecture);

    Optional<Lecture> getLectureById(Long id);

    List<Lecture> getAllLectures();

    boolean deleteLectureById(Long id);

    List<Lecture> saveAllLectures(List<Lecture> lectures);

    boolean addLessonToLecture(Lesson lesson, Lecture lecture);

    boolean removeLessonFromLecture(Lesson lesson, Lecture lecture);

    boolean addTeacherToLecture(Teacher teacher, Lecture lecture);

    boolean removeTeacherFromLecture(Teacher teacher, Lecture lecture);
}
