package com.foxminded.university.management.schedule.controllers.web;

import com.foxminded.university.management.schedule.controllers.web.utils.StringUtils;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
public class GroupController {
    private final GroupService groupService;
    private final LessonService lessonService;
    private final SubjectService subjectService;
    private final AudienceService audienceService;
    private final TeacherService teacherService;
    private final FacultyService facultyService;

    public GroupController(GroupService groupService, LessonService lessonService, SubjectService subjectService,
                           AudienceService audienceService, TeacherService teacherService, FacultyService facultyService) {
        this.groupService = groupService;
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

        List<Student> students = group.getStudents();
        model.addAttribute("students", students);

        List<Lecture> lectures = group.getLectures();
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
        model.addAttribute("faculty", group.getFaculty());
        return "group";
    }

    @PostMapping("/groups/delete/{id}")
    public String deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroupById(id);
        return "redirect:/groups";
    }

    @PostMapping("/groups/add")
    public String addGroup(@Valid @ModelAttribute Group group, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnAdd", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("groupWithErrors",
                    new Group(group.getName(), group.getFaculty(), group.getStudents(), group.getLectures()));
            return "redirect:/groups";
        }
        try {
            groupService.saveGroup(group);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("group", new Group());
            redirectAttributes.addFlashAttribute("newGroup", group);
            redirectAttributes.addFlashAttribute("allFaculties", facultyService.getAllFaculties());
            redirectAttributes.addFlashAttribute("serviceExceptionOnAdd", e);
            return "redirect:/groups";
        }
        return "redirect:/groups";
    }

    @PostMapping("/groups/update/{id}")
    public String updateGroup(@Valid @ModelAttribute Group group, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnUpdate", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("groupWithErrors",
                    new Group(group.getId(), group.getName(), group.getFaculty(), group.getStudents(), group.getLectures()));
            return "redirect:/groups";
        }
        try {
            groupService.saveGroup(group);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("group", new Group());
            redirectAttributes.addFlashAttribute("newGroup", group);
            redirectAttributes.addFlashAttribute("allFaculties", facultyService.getAllFaculties());
            redirectAttributes.addFlashAttribute("serviceExceptionOnUpdate", e);
            return "redirect:/groups";
        }
        return "redirect:/groups";
    }
}
