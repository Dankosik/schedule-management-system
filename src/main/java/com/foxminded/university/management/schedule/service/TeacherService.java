package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    Teacher saveTeacher(Teacher teacher);

    Optional<Teacher> getTeacherById(Long id);

    List<Teacher> getAllTeachers();

    boolean deleteTeacherById(Long id);

    List<Teacher> saveAllTeachers(List<Teacher> teachers);
}
