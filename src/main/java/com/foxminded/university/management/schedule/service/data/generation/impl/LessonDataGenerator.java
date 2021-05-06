package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.service.SubjectService;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import com.foxminded.university.management.schedule.service.data.generation.utils.RandomUtils;
import com.foxminded.university.management.schedule.service.data.generation.utils.ReceivingIdUtils;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonDataGenerator implements DataGenerator<Lesson> {
    private final SubjectService subjectService;
    private final List<Time> startTimes = List.of(
            Time.valueOf(LocalTime.of(8, 30, 0)),
            Time.valueOf(LocalTime.of(10, 10, 0)),
            Time.valueOf(LocalTime.of(11, 50, 0)),
            Time.valueOf(LocalTime.of(13, 30, 0)),
            Time.valueOf(LocalTime.of(16, 10, 0)));

    public LessonDataGenerator(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public List<Lesson> generateData() {
        List<Lesson> result = new ArrayList<>();
        List<Long> subjectIds = ReceivingIdUtils.getSubjectIds();
        for (int i = 0; i < RandomUtils.random(4, 10); i++) {
            Lesson lesson = new Lesson();
            lesson.setDuration(Duration.ofMinutes(90));
            lesson.setNumber(1 + (int) (Math.random() * 5));
            lesson.setStartTime(startTimes.get(RandomUtils.random(0, startTimes.size() - 1)));
            lesson.setSubject(subjectService.getSubjectById(subjectIds.get(RandomUtils.random(0, subjectIds.size() - 1))));
            result.add(lesson);
        }
        return result;
    }
}
