package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher saveTeacher(Teacher teacher);

    Teacher getTeacherById(Long id);

    List<Teacher> getAllTeachers();

    void deleteTeacherById(Long id);

    List<Teacher> saveAllTeachers(List<Teacher> teachers);

    List<String> getLastNameWithInitialsForAllTeachers(List<Teacher> teachers);

    List<Teacher> getTeachersForLectures(List<Lecture> lectures);
}
