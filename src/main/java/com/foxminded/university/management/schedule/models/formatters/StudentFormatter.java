package com.foxminded.university.management.schedule.models.formatters;

import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.StudentService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class StudentFormatter implements Formatter<Student> {
    private final StudentService studentService;

    public StudentFormatter(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public Student parse(String s, Locale locale) throws ParseException {
        return studentService.getStudentById(Long.valueOf(s));
    }

    @Override
    public String print(Student student, Locale locale) {
        return student.toString();
    }
}
