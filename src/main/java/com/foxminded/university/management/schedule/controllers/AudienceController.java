package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.StringUtils;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.*;
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
public class AudienceController {
    private final AudienceServiceImpl audienceService;
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;
    private final TeacherServiceImpl teacherService;
    private final GroupServiceImpl groupService;

    public AudienceController(AudienceServiceImpl audienceService, LessonServiceImpl lessonService, SubjectServiceImpl subjectService,
                              TeacherServiceImpl teacherService, GroupServiceImpl groupService) {
        this.audienceService = audienceService;
        this.lessonService = lessonService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.groupService = groupService;
    }

    @GetMapping("/audiences")
    public String showAllAudiences(Model model) {
        model.addAttribute("audiences", audienceService.getAllAudiences());
        model.addAttribute("audience", new Audience());
        return "audiences";
    }

    @GetMapping("/audiences/{id}")
    public String showOneAudience(@PathVariable("id") Long id, Model model) {
        Audience audience = audienceService.getAudienceById(id);
        model.addAttribute("audience", audience);

        List<Lecture> lectures = audience.getLectures();
        model.addAttribute("lectures", lectures);

        List<Lesson> lessons = lessonService.getLessonsWithPossibleNullForLectures(lectures);
        List<String> formattedDurations = StringUtils.formatListOfDurations(lessonService.getDurationsWithPossibleNullForLessons(lessons));

        model.addAttribute("durations", formattedDurations);
        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("startTimes", lessonService.getStartTimesWithPossibleNullForLessons(lessons));

        List<Teacher> teachers = teacherService.getTeachersWithPossibleNullForLectures(lectures);
        model.addAttribute("teachers", teachers);
        model.addAttribute("teacherNames", teacherService.getLastNamesWithInitialsWithPossibleNullForTeachers(teachers));

        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));

        model.addAttribute("groupNames", groupService.getGroupNamesForLectures(lectures));
        model.addAttribute("groups", groupService.getGroupsForLectures(lectures));

        model.addAttribute("lecture", new Lecture());

        model.addAttribute("allTeachers", teacherService.getAllTeachers());
        model.addAttribute("allAudiences", audienceService.getAllAudiences());
        model.addAttribute("allGroups", groupService.getAllGroups());

        List<Lesson> allLessons = lessonService.getAllLessons();
        model.addAttribute("allLessons", allLessons);

        List<String> formattedDurationsForAllLessons = StringUtils.formatListOfDurations(lessonService.getDurationsWithPossibleNullForLessons(allLessons));
        model.addAttribute("durationsForAllLessons", formattedDurationsForAllLessons);
        model.addAttribute("subjectsForAllLessons", subjectService.getSubjectsWithPossibleNullForLessons(allLessons));
        return "audience";
    }

    @PostMapping("/audiences/delete/{id}")
    public String deleteAudience(@PathVariable("id") Long id) {
        audienceService.deleteAudienceById(id);
        return "redirect:/audiences";
    }

    @PostMapping("/audiences/add")
    public String addAudience(@Valid @ModelAttribute Audience audience, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrors", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("audienceWithErrors",
                    new Audience(audience.getNumber(), audience.getCapacity(), audience.getLectures()));
            return "redirect:/audiences";
        }
        try {
            audienceService.saveAudience(audience);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("newAudience", audience);
            redirectAttributes.addFlashAttribute("audience", new Audience());
            redirectAttributes.addFlashAttribute("serviceExceptionOnAdd", e);
            return "redirect:/audiences";
        }
        return "redirect:/audiences";
    }

    @PostMapping("/audiences/update/{id}")
    public String updateAudience(@Valid @ModelAttribute Audience audience, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnUpdate", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("audienceWithErrors",
                    new Audience(audience.getId(), audience.getNumber(), audience.getCapacity(), audience.getLectures()));
            return "redirect:/audiences";
        }
        try {
            audienceService.saveAudience(audience);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("newAudience", audience);
            redirectAttributes.addFlashAttribute("audience", new Audience());
            redirectAttributes.addFlashAttribute("serviceExceptionOnUpdate", e);
            return "redirect:/audiences";
        }
        return "redirect:/audiences";
    }
}
