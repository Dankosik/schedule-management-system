package com.foxminded.university.management.schedule.controllers.web;

import com.foxminded.university.management.schedule.controllers.web.utils.StringUtils;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Teacher;
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
public class LectureController {
    private final LectureService lectureService;
    private final AudienceService audienceService;
    private final LessonService lessonService;
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final GroupService groupService;

    public LectureController(LectureService lectureService, AudienceService audienceService, LessonService lessonService,
                             TeacherService teacherService, SubjectService subjectService, GroupService groupService) {
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

        List<Lesson> lessons = lessonService.getLessonsWithPossibleNullForLectures(lectures);

        model.addAttribute("durations", StringUtils.formatListOfDurations(lessonService.getDurationsWithPossibleNullForLessons(lessons)));
        model.addAttribute("subjectNames", subjectService.getSubjectNamesForLessons(lessons));
        model.addAttribute("startTimes", lessonService.getStartTimesWithPossibleNullForLessons(lessons));

        List<Teacher> teachers = teacherService.getTeachersWithPossibleNullForLectures(lectures);
        model.addAttribute("teachers", teachers);
        model.addAttribute("allTeachers", teacherService.getAllTeachers());
        model.addAttribute("teacherNames", teacherService.getLastNamesWithInitialsWithPossibleNullForTeachers(teachers));

        List<Audience> audiences = audienceService.getAudiencesWithPossibleNullForLectures(lectures);
        model.addAttribute("audiences", audiences);
        model.addAttribute("allAudiences", audienceService.getAllAudiences());
        model.addAttribute("audienceNumbers", audienceService.getAudienceNumbersWithPossibleNullForAudiences(audiences));

        model.addAttribute("subjects", subjectService.getSubjectsForLectures(lectures));
        model.addAttribute("subjectsForLessons", subjectService.getSubjectsWithPossibleNullForLessons(lessons));

        model.addAttribute("groupNames", groupService.getGroupNamesForLectures(lectures));
        model.addAttribute("allGroups", groupService.getAllGroups());
        model.addAttribute("groups", groupService.getGroupsForLectures(lectures));

        model.addAttribute("lecture", new Lecture());

        List<Lesson> allLessons = lessonService.getAllLessons();
        model.addAttribute("allLessons", allLessons);
        model.addAttribute("durationsForAllLessons", StringUtils.formatListOfDurations(lessonService.getDurationsWithPossibleNullForLessons(allLessons)));
        model.addAttribute("subjectsForAllLessons", subjectService.getSubjectsWithPossibleNullForLessons(allLessons));
        return "lectures";
    }

    @PostMapping("/lectures/delete/{id}")
    public String deleteLecture(@PathVariable("id") Long id) {
        lectureService.deleteLectureById(id);
        return "redirect:/lectures";
    }

    @PostMapping("/lectures/add")
    public String addLecture(@Valid @ModelAttribute Lecture lecture, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnAdd", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("lectureWithErrors",
                    new Lecture(lecture.getNumber(), lecture.getDate(), lecture.getAudience(), lecture.getGroup(), lecture.getLesson(), lecture.getTeacher()));
            return "redirect:/lectures";
        }
        lecture.setNumber(lecture.getLesson().getNumber());
        lectureService.saveLecture(lecture);
        return "redirect:/lectures";
    }

    @PostMapping("/lectures/update/{id}")
    public String updateLecture(@Valid @ModelAttribute Lecture lecture, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnUpdate", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("lectureWithErrors",
                    new Lecture(lecture.getId(), lecture.getNumber(), lecture.getDate(), lecture.getAudience(), lecture.getGroup(), lecture.getLesson(), lecture.getTeacher()));
            return "redirect:/lectures";
        }
        lecture.setNumber(lecture.getLesson().getNumber());
        lectureService.saveLecture(lecture);
        return "redirect:/lectures";
    }
}
