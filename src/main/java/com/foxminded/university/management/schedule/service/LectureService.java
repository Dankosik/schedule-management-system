package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Lecture;

import java.util.List;

public interface LectureService {
    Lecture saveLecture(Lecture lecture);

    Lecture getLectureById(Long id);

    List<Lecture> getAllLectures();

    void deleteLectureById(Long id);

    List<Lecture> saveAllLectures(List<Lecture> lectures);
}
