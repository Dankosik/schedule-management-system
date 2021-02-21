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
public class LectureController {
    private final LectureServiceImpl lectureService;
    private final AudienceServiceImpl audienceService;
    private final LessonServiceImpl lessonService;
    private final TeacherServiceImpl teacherService;
    private final SubjectServiceImpl subjectService;
    private final GroupServiceImpl groupService;

    public LectureController(LectureServiceImpl lectureService, AudienceServiceImpl audienceService, LessonServiceImpl lessonService,
                             TeacherServiceImpl teacherService, SubjectServiceImpl subjectService, GroupServiceImpl groupService) {
        this.lectureService = lectureService;
        this.audienceService = audienceService;
        this.lessonService = lessonService;
        this.teacherService = teacherService;
        this.subjectService = subjectService;
        this.groupService = groupService;
    }

    @GetMapping("/lectures")
    public String showAllLectures(Model model) {
        List<Lecture> lectures = lectureService.getAllLectures();
        model.addAttribute("lectures", lectures);

        List<Lesson> lessons = lessonService.getLessonsForLectures(lectures);

        model.addAttribute("durations", StringUtils.formatListOfDurations(lessonService.getDurationsForLessons(lessons)));
        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("startTimes", lessonService.getStartTimesForLessons(lessons));

        List<Teacher> teachers = teacherService.getTeachersForLectures(lectures);
        model.addAttribute("teachers", teachers);
        model.addAttribute("allTeachers", teacherService.getAllTeachers());
        model.addAttribute("teacherNames", teacherService.getLastNamesWithInitialsForTeachers(teachers));

        List<Audience> audiences = audienceService.getAudiencesForLectures(lectures);
        model.addAttribute("audiences", audiences);
        model.addAttribute("allAudiences", audienceService.getAllAudiences());
        model.addAttribute("audienceNumbers", audienceService.getAudienceNumbersForAudiences(audiences));

        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));
        model.addAttribute("subjectsForLessons", subjectService.getSubjectsForLessons(lessons));

        model.addAttribute("groupNames", groupService.getGroupNamesForLectures(lectures));
        model.addAttribute("allGroups", groupService.getAllGroups());
        model.addAttribute("groups", groupService.getGroupsForLectures(lectures));

        model.addAttribute("lecture", new Lecture());

        List<Lesson> allLessons = lessonService.getAllLessons();
        model.addAttribute("allLessons", allLessons);
        model.addAttribute("durationsForAllLessons", StringUtils.formatListOfDurations(lessonService.getDurationsForLessons(allLessons)));
        model.addAttribute("subjectsForAllLessons", subjectService.getSubjectsForLessons(allLessons));
        return "lectures";
    }

    @PostMapping("/lectures/delete/{id}")
    public String deleteLecture(@PathVariable("id") Long id) {
        lectureService.deleteLectureById(id);
        return "redirect:/lectures";
    }

    @PostMapping("/lectures/add")
    public String addLecture(@ModelAttribute Lecture lecture) {
        lectureService.saveLecture(lecture);
        return "redirect:/lectures";
    }

    @PostMapping("/lectures/update/{id}")
    public String updateLecture(@ModelAttribute Lecture lecture) {
        lectureService.saveLecture(lecture);
        return "redirect:/lectures";
    }
}
