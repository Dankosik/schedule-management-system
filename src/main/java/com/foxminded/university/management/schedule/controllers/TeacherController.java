package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.StringUtils;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class TeacherController {
    private final TeacherServiceImpl teacherService;
    private final FacultyServiceImpl facultyService;
    private final AudienceServiceImpl audienceService;
    private final LectureServiceImpl lectureService;
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;
    private final GroupServiceImpl groupService;


    public TeacherController(TeacherServiceImpl teacherService, FacultyServiceImpl facultyService,
                             AudienceServiceImpl audienceService, LectureServiceImpl lectureService,
                             LessonServiceImpl lessonService, SubjectServiceImpl subjectService, GroupServiceImpl groupService) {
        this.teacherService = teacherService;
        this.facultyService = facultyService;
        this.audienceService = audienceService;
        this.lectureService = lectureService;
        this.lessonService = lessonService;
        this.subjectService = subjectService;
        this.groupService = groupService;
    }

    @GetMapping("/teachers")
    public String showAllFaculties(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        model.addAttribute("facultyNames", facultyService.getFacultyNamesForTeachers(teachers));
        model.addAttribute("faculties", facultyService.getFacultiesForTeachers(teachers));

        model.addAttribute("teacher", new Teacher());
        model.addAttribute("allFaculties", facultyService.getAllFaculties());
        return "teachers";
    }

    @GetMapping("/teachers/{id}")
    public String showOneTeacher(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("faculty", facultyService.getFacultyById(teacher.getFacultyId()));

        List<Lecture> lectures = lectureService.getLecturesForTeacher(teacher);
        model.addAttribute("lectures", lectures);
        model.addAttribute("lecturesDate", lectureService.getLectureDateWithPossibleNullForLectures(lectures));

        List<Lesson> lessons = lessonService.getLessonsWithPossibleNullForLectures(lectures);

        model.addAttribute("durations", StringUtils.formatListOfDurations(lessonService.getDurationsWithPossibleNullForLessons(lessons)));
        model.addAttribute("startTimes", lessonService.getStartTimesWithPossibleNullForLessons(lessons));

        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));
        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));

        model.addAttribute("groupNames", groupService.getGroupNamesForLectures(lectures));
        model.addAttribute("groups", groupService.getGroupsForLectures(lectures));

        List<Audience> audiences = audienceService.getAudiencesWithPossibleNullForLectures(lectures);
        model.addAttribute("audiences", audiences);
        model.addAttribute("audienceNumbers", audienceService.getAudienceNumbersWithPossibleNullForAudiences(audiences));

        model.addAttribute("lecture", new Lecture());
        model.addAttribute("allFaculties", facultyService.getAllFaculties());
        model.addAttribute("allTeachers", teacherService.getAllTeachers());
        model.addAttribute("allAudiences", audienceService.getAllAudiences());
        model.addAttribute("allGroups", groupService.getAllGroups());

        List<Lesson> allLessons = lessonService.getAllLessons();
        model.addAttribute("allLessons", allLessons);

        List<String> formattedDurationsForAllLessons = StringUtils.formatListOfDurations(lessonService.getDurationsWithPossibleNullForLessons(allLessons));
        model.addAttribute("durationsForAllLessons", formattedDurationsForAllLessons);
        model.addAttribute("subjectsForAllLessons", subjectService.getSubjectsWithPossibleNullForLessons(allLessons));
        return "teacher";
    }

    @PostMapping("/teachers/delete/{id}")
    public String deleteAudience(@PathVariable("id") Long id) {
        teacherService.deleteTeacherById(id);
        return "redirect:/teachers";
    }

    @PostMapping("/teachers/add")
    public String addStudent(@ModelAttribute Teacher teacher) {
        teacherService.saveTeacher(teacher);
        return "redirect:/teachers";
    }

    @PostMapping("/teachers/update/{id}")
    public String updateTeacher(@ModelAttribute Teacher teacher) {
        teacherService.saveTeacher(teacher);
        return "redirect:/teachers";
    }
}
