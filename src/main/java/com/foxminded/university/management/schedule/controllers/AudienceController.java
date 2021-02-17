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

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AudienceController {
    private final AudienceServiceImpl audienceService;
    private final LectureServiceImpl lectureService;
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;
    private final TeacherServiceImpl teacherService;
    private final GroupServiceImpl groupService;

    public AudienceController(AudienceServiceImpl audienceService, LectureServiceImpl lectureService,
                              LessonServiceImpl lessonService, SubjectServiceImpl subjectService,
                              TeacherServiceImpl teacherService, GroupServiceImpl groupService) {
        this.audienceService = audienceService;
        this.lectureService = lectureService;
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
        List<Lecture> lectures = lectureService.getLecturesForAudience(audience);
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

        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));
        model.addAttribute("groupNames", groupService.getGroupNamesForLectures(lectures));
        model.addAttribute("groups", groupService.getGroupsForLectures(lectures));
        return "audience";
    }

    @PostMapping("/audiences/delete/{id}")
    public String deleteAudience(@PathVariable("id") Long id) {
        audienceService.deleteAudienceById(id);
        return "redirect:/audiences";
    }

    @PostMapping("/audiences/add")
    public String addAudience(@ModelAttribute Audience audience) {
        audienceService.saveAudience(audience);
        return "redirect:/audiences";
    }
}
