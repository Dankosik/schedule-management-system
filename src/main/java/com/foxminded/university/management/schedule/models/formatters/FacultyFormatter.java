package com.foxminded.university.management.schedule.models.formatters;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.service.FacultyService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class FacultyFormatter implements Formatter<Faculty> {
    private final FacultyService facultyService;

    public FacultyFormatter(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @Override
    public Faculty parse(String s, Locale locale) throws ParseException {
        return facultyService.getFacultyById(Long.valueOf(s));
    }

    @Override
    public String print(Faculty faculty, Locale locale) {
        return faculty.toString();
    }
}
