package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.StringUtils;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.impl.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class GroupController {
    private final GroupServiceImpl groupService;
    private final StudentServiceImpl studentService;
    private final LectureServiceImpl lectureService;
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;
    private final AudienceServiceImpl audienceService;
    private final TeacherServiceImpl teacherService;
    private final FacultyServiceImpl facultyService;

    public GroupController(GroupServiceImpl groupService, StudentServiceImpl studentService,
                           LectureServiceImpl lectureService, LessonServiceImpl lessonService,
                           SubjectServiceImpl subjectService, AudienceServiceImpl audienceService, TeacherServiceImpl teacherService,
                           FacultyServiceImpl facultyService) {
        this.groupService = groupService;
        this.studentService = studentService;
        this.lectureService = lectureService;
        this.lessonService = lessonService;
        this.subjectService = subjectService;
        this.audienceService = audienceService;
        this.teacherService = teacherService;
        this.facultyService = facultyService;
    }

    @GetMapping("/groups")
    public String showAllGroups(Model model) {
        List<Group> allGroups = groupService.getAllGroups();
        model.addAttribute("allGroups", allGroups);
        model.addAttribute("group", new Group());
        model.addAttribute("faculties", facultyService.getFacultiesForGroups(allGroups));
        model.addAttribute("allFaculties", facultyService.getAllFaculties());
        return "groups";
    }

    @GetMapping("/groups/{id}")
    public String showOneGroup(@PathVariable("id") Long id, Model model) {
        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);

        model.addAttribute("students", studentService.getStudentsForGroup(group));

        List<Lecture> lectures = lectureService.getLecturesForGroup(group);
        model.addAttribute("lectures", lectures);

        List<Lesson> lessons = lessonService.getLessonsWithPossibleNullForLectures(lectures);

        model.addAttribute("durations", StringUtils.formatListOfDurations(lessonService.getDurationsWithPossibleNullForLessons(lessons)));
        model.addAttribute("startTimes", lessonService.getStartTimesWithPossibleNullForLessons(lessons));

        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));

        List<Teacher> teachers = teacherService.getTeachersWithPossibleNullForLectures(lectures);
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherNames", teacherService.getLastNamesWithInitialsWithPossibleNullForTeachers(teachers));

        List<Audience> audiences = audienceService.getAudiencesWithPossibleNullForLectures(lectures);
        model.addAttribute("audiences", audiences);
        model.addAttribute("audienceNumbers", audienceService.getAudienceNumbersWithPossibleNullForAudiences(audiences));

        model.addAttribute("lecture", new Lecture());
        model.addAttribute("student", new Student());

        model.addAttribute("allTeachers", teacherService.getAllTeachers());
        model.addAttribute("allAudiences", audienceService.getAllAudiences());
        model.addAttribute("allGroups", groupService.getAllGroups());

        List<Lesson> allLessons = lessonService.getAllLessons();
        model.addAttribute("allLessons", allLessons);

        List<String> formattedDurationsForAllLessons = StringUtils.formatListOfDurations(lessonService.getDurationsWithPossibleNullForLessons(allLessons));
        model.addAttribute("durationsForAllLessons", formattedDurationsForAllLessons);
        model.addAttribute("subjectsForAllLessons", subjectService.getSubjectsWithPossibleNullForLessons(allLessons));
        model.addAttribute("faculties", facultyService.getAllFaculties());
        model.addAttribute("faculty", facultyService.getFacultyForGroup(group));
        model.addAttribute("subjectService", subjectService);
        return "group";
    }

    @PostMapping("/groups/delete/{id}")
    public String deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroupById(id);
        return "redirect:/groups";
    }

    @PostMapping("/groups/add")
    public String addGroup(@ModelAttribute Group group, Model model) {
        try {
            groupService.saveGroup(group);
        } catch (ServiceException e) {
            model.addAttribute("group", new Group());
            model.addAttribute("newGroup", group);
            model.addAttribute("allFaculties", facultyService.getAllFaculties());
            model.addAttribute("exception", e);
            return "error/group-add-error-page";
        }
        return "redirect:/groups";
    }

    @PostMapping("/groups/update/{id}")
    public String updateGroup(@ModelAttribute Group group, Model model) {
        try {
            groupService.saveGroup(group);
        } catch (ServiceException e) {
            model.addAttribute("group", new Group());
            model.addAttribute("newGroup", group);
            model.addAttribute("allFaculties", facultyService.getAllFaculties());
            model.addAttribute("exception", e);
            return "error/group-edit-error-page";
        }
        return "redirect:/groups";
    }
}
