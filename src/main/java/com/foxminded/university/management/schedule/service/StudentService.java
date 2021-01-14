package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student saveStudent(Student student);

    Optional<Student> getStudentById(Long id);

    List<Student> getAllStudent();

    boolean deleteStudentById(Long id);

    List<Student> saveAllStudents(List<Student> students);
}
