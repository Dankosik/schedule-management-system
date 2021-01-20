package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;

import java.util.List;

public interface LectureService {
    Lecture saveLecture(Lecture lecture);

    Lecture getLectureById(Long id);

    List<Lecture> getAllLectures();

    void deleteLectureById(Long id);

    List<Lecture> saveAllLectures(List<Lecture> lectures);

    Lecture addLessonToLecture(Lesson lesson, Lecture lecture);

    Lecture removeLessonFromLecture(Lesson lesson, Lecture lecture);

    Lecture addTeacherToLecture(Teacher teacher, Lecture lecture);

    Lecture removeTeacherFromLecture(Teacher teacher, Lecture lecture);
}
