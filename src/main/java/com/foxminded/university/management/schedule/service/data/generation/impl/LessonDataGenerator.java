package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import com.foxminded.university.management.schedule.service.data.generation.utils.ReceivingIdUtils;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class LessonDataGenerator implements DataGenerator<Lesson> {
    private final List<Time> startTimes = List.of(
            Time.valueOf(LocalTime.of(8, 30, 0)),
            Time.valueOf(LocalTime.of(10, 10, 0)),
            Time.valueOf(LocalTime.of(11, 50, 0)),
            Time.valueOf(LocalTime.of(13, 30, 0)),
            Time.valueOf(LocalTime.of(16, 10, 0)));

    @Override
    public List<Lesson> generateData() {
        List<Lesson> result = new ArrayList<>();
        List<Long> subjectIds = ReceivingIdUtils.getSubjectIds();
        for (int i = 0; i < 1 + (int) (Math.random() * 10); i++) {
            Lesson lesson = new Lesson();
            lesson.setDuration(Duration.ofMinutes(90));
            lesson.setNumber(1 + (int) (Math.random() * 5));
            lesson.setStartTime(startTimes.get((int) (Math.random() * startTimes.size())));
            lesson.setSubjectId(subjectIds.get((int) (Math.random() * subjectIds.size())));
            result.add(lesson);
        }
        return result;
    }
}
