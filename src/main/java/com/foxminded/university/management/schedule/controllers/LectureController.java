package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.StringUtils;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.impl.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LectureController {
    private final LectureServiceImpl lectureService;
    private final AudienceServiceImpl audienceService;
    private final LessonServiceImpl lessonService;
    private final TeacherServiceImpl teacherService;
    private final SubjectServiceImpl subjectService;
    private final GroupServiceImpl groupService;
    private final StudentServiceImpl studentService;

    public LectureController(LectureServiceImpl lectureService, AudienceServiceImpl audienceService, LessonServiceImpl lessonService,
                             TeacherServiceImpl teacherService, SubjectServiceImpl subjectService, GroupServiceImpl groupService, StudentServiceImpl studentService) {
        this.lectureService = lectureService;
        this.audienceService = audienceService;
        this.lessonService = lessonService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @GetMapping("/lectures")
    public String showAllLectures(Model model) {
        List<Lecture> lectures = lectureService.getAllLectures();
        model.addAttribute("lectures", lectures);

        List<Lesson> lessons = lessonService.getLessonsForLectures(lectures);
        List<Duration> durations = lessonService.getDurationsForLessons(lessons);
        List<String> formattedDurations = durations.stream()
                .map(StringUtils::formatDurationInMinutes)
                .collect(Collectors.toList());

        model.addAttribute("durations", formattedDurations);
        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("startTimes", lessonService.getStartTimesForLessons(lessons));

        List<Teacher> teachers = teacherService.getTeachersForLectures(lectures);
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherNames", teacherService.getLastNamesWithInitialsForTeachers(teachers));

        List<Audience> audiences = audienceService.getAudiencesForLectures(lectures);
        model.addAttribute("audiences", audiences);
        model.addAttribute("audienceNumbers", audienceService.getAudienceNumbersForAudiences(audiences));

        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));

        List<Student> students = studentService.getAllStudents();
        model.addAttribute("groupNames", groupService.getGroupNamesForStudents(students));
        model.addAttribute("groups", groupService.getGroupsForStudents(students));
        return "lectures";
    }
}
