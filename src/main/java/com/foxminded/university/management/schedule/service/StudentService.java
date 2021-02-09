package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Student;

import java.util.List;

public interface StudentService {
    Student saveStudent(Student student);

    Student getStudentById(Long id);

    List<Student> getAllStudents();

    void deleteStudentById(Long id);

    List<Student> saveAllStudents(List<Student> students);
}
