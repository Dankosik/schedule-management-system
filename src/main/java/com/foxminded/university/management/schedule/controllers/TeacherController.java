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
public class TeacherController {
    private final TeacherServiceImpl teacherService;
    private final FacultyServiceImpl facultyService;
    private final AudienceServiceImpl audienceService;
    private final LectureServiceImpl lectureService;
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;
    private final StudentServiceImpl studentService;
    private final GroupServiceImpl groupService;


    public TeacherController(TeacherServiceImpl teacherService, FacultyServiceImpl facultyService, AudienceServiceImpl audienceService, LectureServiceImpl lectureService, LessonServiceImpl lessonService, SubjectServiceImpl subjectService, StudentServiceImpl studentService, GroupServiceImpl groupService) {
        this.teacherService = teacherService;
        this.facultyService = facultyService;
        this.audienceService = audienceService;
        this.lectureService = lectureService;
        this.lessonService = lessonService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("/teachers")
    public String showAllFaculties(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        model.addAttribute("facultyNames", facultyService.getFacultyNamesForTeachers(teachers));
        model.addAttribute("faculties", facultyService.getFacultiesForTeachers(teachers));
        return "teachers";
    }

    @GetMapping("/teachers/{id}")
    public String showOneTeacher(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("faculty", facultyService.getFacultyById(teacher.getFacultyId()));

        List<Lecture> lectures = lectureService.getLecturesForTeacher(teacher);
        model.addAttribute("lectures", lectures);

        List<Lesson> lessons = lessonService.getLessonsForLectures(lectures);
        List<Duration> durations = lessonService.getDurationsForLessons(lessons);
        List<String> formattedDurations = durations.stream()
                .map(StringUtils::formatDurationInMinutes)
                .collect(Collectors.toList());

        model.addAttribute("durations", formattedDurations);
        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("startTimes", lessonService.getStartTimesForLessons(lessons));

        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));

        List<Student> students = studentService.getAllStudents();
        model.addAttribute("groupNames", groupService.getGroupNamesForStudents(students));
        model.addAttribute("groups", groupService.getGroupsForStudents(students));
        List<Audience> audiences = audienceService.getAudiencesForLectures(lectures);

        model.addAttribute("audiences", audiences);
        model.addAttribute("audienceNumbers", audienceService.getAudienceNumbersForAudiences(audiences));
        return "teacher";
    }
}
