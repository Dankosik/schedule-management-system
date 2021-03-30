package com.foxminded.university.management.schedule.models.formatters;

import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class TeacherFormatter implements Formatter<Teacher> {
    private final TeacherServiceImpl teacherService;

    public TeacherFormatter(TeacherServiceImpl teacherService) {
        this.teacherService = teacherService;
    }

    @Override
    public Teacher parse(String s, Locale locale) throws ParseException {
        return teacherService.getTeacherById(Long.valueOf(s));
    }

    @Override
    public String print(Teacher teacher, Locale locale) {
        return teacher.toString();
    }
}
