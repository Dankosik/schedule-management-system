package com.foxminded.university.management.schedule.service.data.generation.utils;

import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceivingIdUtils {
    private static FacultyService facultyService;
    private static GroupService groupService;
    private static SubjectService subjectService;
    private static TeacherService teacherService;
    private static LessonService lessonService;
    private static AudienceService audienceService;

    public ReceivingIdUtils(FacultyService facultyService, GroupService groupService, SubjectService subjectService, TeacherService teacherService, LessonService lessonService, AudienceService audienceService) {
        ReceivingIdUtils.facultyService = facultyService;
        ReceivingIdUtils.groupService = groupService;
        ReceivingIdUtils.subjectService = subjectService;
        ReceivingIdUtils.teacherService = teacherService;
        ReceivingIdUtils.lessonService = lessonService;
        ReceivingIdUtils.audienceService = audienceService;
    }

    public static List<Long> getFacultyIds() {
        return facultyService.getAllFaculties()
                .stream()
                .map(Faculty::getId)
                .collect(Collectors.toList());
    }

    public static List<Long> getGroupIds() {
        return groupService.getAllGroups()
                .stream()
                .map(Group::getId)
                .collect(Collectors.toList());
    }

    public static List<Long> getSubjectIds() {
        return subjectService.getAllSubjects()
                .stream()
                .map(Subject::getId)
                .collect(Collectors.toList());
    }

    public static List<Long> getTeacherIds() {
        return teacherService.getAllTeachers()
                .stream()
                .map(Teacher::getId)
                .collect(Collectors.toList());
    }

    public static List<Long> getAudienceIds() {
        return audienceService.getAllAudiences()
                .stream()
                .map(Audience::getId)
                .collect(Collectors.toList());
    }

    public static List<Long> getLessonIds() {
        return lessonService.getAllLessons()
                .stream()
                .map(Lesson::getId)
                .collect(Collectors.toList());
    }
}
