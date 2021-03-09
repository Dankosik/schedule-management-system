package com.foxminded.university.management.schedule.service.data.generation.utils;

import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.impl.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReceivingIdUtils {
    private static FacultyServiceImpl facultyService;
    private static GroupServiceImpl groupService;
    private static SubjectServiceImpl subjectService;
    private static TeacherServiceImpl teacherService;
    private static LessonServiceImpl lessonService;
    private static AudienceServiceImpl audienceService;

    public ReceivingIdUtils(FacultyServiceImpl facultyService, GroupServiceImpl groupService, SubjectServiceImpl subjectService, TeacherServiceImpl teacherService, LessonServiceImpl lessonService, AudienceServiceImpl audienceService) {
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
