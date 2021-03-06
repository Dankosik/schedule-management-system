package com.foxminded.university.management.schedule.models.formatters;

import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.service.LessonService;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class LessonFormatter implements Formatter<Lesson> {
    private final LessonService lessonService;

    public LessonFormatter(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @Override
    public Lesson parse(String s, Locale locale) throws ParseException {
        return lessonService.getLessonById(Long.valueOf(s));
    }

    @Override
    public String print(Lesson lesson, Locale locale) {
        return lesson.toString();
    }
}
