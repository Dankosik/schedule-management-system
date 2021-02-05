package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.StringUtils;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.service.LessonService;
import com.foxminded.university.management.schedule.service.SubjectService;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class LessonController {
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;

    public LessonController(LessonServiceImpl lessonService, SubjectServiceImpl subjectService) {
        this.lessonService = lessonService;
        this.subjectService = subjectService;
    }

    @GetMapping("/lessons")
    public String showAllFaculties(Model model) {
        List<Lesson> lessons = lessonService.getAllLessons();
        model.addAttribute("lessons", lessons);

        List<Duration> durations = lessonService.getDurationsForLessons(lessons);
        List<String> formattedDurations = durations.stream()
                .map(StringUtils::formatDuration)
                .collect(Collectors.toList());

        model.addAttribute("durations", formattedDurations);
        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("subjects", subjectService.getSubjectsForLessons(lessons));
        return "lessons";
    }

}
