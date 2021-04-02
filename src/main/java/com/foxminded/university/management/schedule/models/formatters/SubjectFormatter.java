package com.foxminded.university.management.schedule.models.formatters;

import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class SubjectFormatter implements Formatter<Subject> {
    private final SubjectServiceImpl subjectService;

    public SubjectFormatter(SubjectServiceImpl subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public Subject parse(String s, Locale locale) throws ParseException {
        return subjectService.getSubjectById(Long.valueOf(s));
    }

    @Override
    public String print(Subject subject, Locale locale) {
        return subject.toString();
    }
}
