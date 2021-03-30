package com.foxminded.university.management.schedule.models.formatters;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.impl.LectureServiceImpl;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class LectureFormatter implements Formatter<Lecture> {
    private final LectureServiceImpl lectureService;

    public LectureFormatter(LectureServiceImpl lectureService) {
        this.lectureService = lectureService;
    }

    @Override
    public Lecture parse(String s, Locale locale) throws ParseException {
        return lectureService.getLectureById(Long.valueOf(s));
    }

    @Override
    public String print(Lecture lecture, Locale locale) {
        return lecture.toString();
    }
}
