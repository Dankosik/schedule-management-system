package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.StringUtils;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.impl.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GroupController {
    private final GroupServiceImpl groupService;
    private final StudentServiceImpl studentService;
    private final LectureServiceImpl lectureService;
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;
    private final AudienceServiceImpl audienceService;
    private final TeacherServiceImpl teacherService;

    public GroupController(GroupServiceImpl groupService, StudentServiceImpl studentService,
                           LectureServiceImpl lectureService, LessonServiceImpl lessonService,
                           SubjectServiceImpl subjectService, AudienceServiceImpl audienceService, TeacherServiceImpl teacherService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.lectureService = lectureService;
        this.lessonService = lessonService;
        this.subjectService = subjectService;
        this.audienceService = audienceService;
        this.teacherService = teacherService;
    }

    @GetMapping("/groups")
    public String showAllGroups(Model model) {
        model.addAttribute("groups", groupService.getAllAGroups());
        return "groups";
    }

    @GetMapping("/groups/{id}")
    public String showOneGroup(@PathVariable("id") Long id, Model model) {
        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);

        model.addAttribute("students", studentService.getStudentsForGroup(group));

        List<Lecture> lectures = lectureService.getLecturesForGroup(group);
        model.addAttribute("lectures", lectures);

        List<Lesson> lessons = lessonService.getLessonsForLectures(lectures);
        List<Duration> durations = lessonService.getDurationsForLessons(lessons);
        List<String> formattedDurations = durations.stream()
                .map(StringUtils::formatDurationInMinutes)
                .collect(Collectors.toList());

        model.addAttribute("durations", formattedDurations);
        model.addAttribute("startTimes", lessonService.getStartTimesForLessons(lessons));

        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));

        List<Teacher> teachers = teacherService.getTeachersForLectures(lectures);
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherNames", teacherService.getLastNamesWithInitialsForTeachers(teachers));

        List<Audience> audiences = audienceService.getAudiencesForLectures(lectures);
        model.addAttribute("audiences", audiences);
        model.addAttribute("audienceNumbers", audienceService.getAudienceNumbersForAudiences(audiences));
        return "group";
    }
}
