package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import com.foxminded.university.management.schedule.service.data.generation.utils.RandomUtils;
import com.foxminded.university.management.schedule.service.data.generation.utils.ReceivingIdUtils;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LectureDataGenerator implements DataGenerator<Lecture> {
    private final LessonServiceImpl lessonService;
    private final TeacherServiceImpl teacherService;
    private final AudienceServiceImpl audienceService;
    private final GroupServiceImpl groupService;
    private final List<Date> dates = List.of(
            Date.valueOf(LocalDate.of(2021, 1, 1)),
            Date.valueOf(LocalDate.of(2021, 1, 2)),
            Date.valueOf(LocalDate.of(2021, 1, 3)));

    public LectureDataGenerator(LessonServiceImpl lessonService, TeacherServiceImpl teacherService, AudienceServiceImpl audienceService, GroupServiceImpl groupService) {
        this.lessonService = lessonService;
        this.teacherService = teacherService;
        this.audienceService = audienceService;
        this.groupService = groupService;
    }

    @Override
    public List<Lecture> generateData() {
        List<Lecture> result = new ArrayList<>();
        List<Long> teacherIds = ReceivingIdUtils.getTeacherIds();
        List<Long> audienceIds = ReceivingIdUtils.getAudienceIds();
        List<Long> lessonIds = ReceivingIdUtils.getLessonIds();
        List<Long> groupIds = ReceivingIdUtils.getGroupIds();
        for (int i = 0; i < lessonService.getAllLessons().size(); i++) {
            Lecture lecture = new Lecture();
            lecture.setDate(dates.get(RandomUtils.random(0, dates.size())));
            lecture.setLesson(lessonService.getLessonById(lessonIds.get(RandomUtils.random(0, lessonIds.size() - 1))));
            lecture.setTeacher(teacherService.getTeacherById(teacherIds.get(RandomUtils.random(0, teacherIds.size() - 1))));
            lecture.setAudience(audienceService.getAudienceById(audienceIds.get(RandomUtils.random(0, audienceIds.size() - 1))));
            lecture.setGroup(groupService.getGroupById(groupIds.get(RandomUtils.random(0, groupIds.size() - 1))));
            lecture.setNumber(RandomUtils.random(1, 5));
            result.add(lecture);
        }
        return result;
    }
}
