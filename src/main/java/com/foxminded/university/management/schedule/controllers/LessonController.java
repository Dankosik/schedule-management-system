package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.controllers.utils.DurationFormatter;
import com.foxminded.university.management.schedule.controllers.utils.StringUtils;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class LessonController {
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;

    public LessonController(LessonServiceImpl lessonService, SubjectServiceImpl subjectService) {
        this.lessonService = lessonService;
        this.subjectService = subjectService;
    }

    @GetMapping("/lessons")
    public String showAllLessons(Model model) {
        List<Lesson> lessons = lessonService.getAllLessons();
        model.addAttribute("lessons", lessons);
        model.addAttribute("durations", StringUtils.formatListOfDurations(lessonService.getDurationsForLessons(lessons)));
        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("subjects", subjectService.getSubjectsForLessons(lessons));
        model.addAttribute("allSubjects", subjectService.getAllSubjects());
        model.addAttribute("lesson", new Lesson());
        return "lessons";
    }

    @PostMapping("/lessons/delete/{id}")
    public String deleteAudience(@PathVariable("id") Long id) {
        lessonService.deleteLessonById(id);
        return "redirect:/lessons";
    }

    @PostMapping("/lessons/add")
    public String addLesson(@ModelAttribute Lesson lesson) {
        lessonService.saveLesson(lesson);
        return "redirect:/lessons";
    }

    @PostMapping("/lessons/update/{id}")
    public String updateLesson(@ModelAttribute Lesson lesson) {
        lessonService.saveLesson(lesson);
        return "redirect:/lessons";
    }
}
