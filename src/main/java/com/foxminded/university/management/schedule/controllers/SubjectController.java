package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SubjectController {
    private final SubjectServiceImpl subjectService;

    public SubjectController(SubjectServiceImpl subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("/subjects")
    public String showAllTeachers(Model model) {
        model.addAttribute("subjects", subjectService.getAllSubjects());
        model.addAttribute("subject", new Subject());
        return "subjects";
    }

    @GetMapping("/subjects/{id}")
    public String showOneTeacher(@PathVariable("id") Long id, Model model) {
        model.addAttribute("subject", subjectService.getSubjectById(id));
        return "subject";
    }

    @PostMapping("/subjects/delete/{id}")
    public String deleteAudience(@PathVariable("id") Long id) {
        subjectService.deleteSubjectById(id);
        return "redirect:/subjects";
    }

    @PostMapping("/subjects/add")
    public String addSubject(@ModelAttribute Subject subject, Model model) {
        try {
            subjectService.saveSubject(subject);
        } catch (ServiceException e) {
            model.addAttribute("subject", new Subject());
            model.addAttribute("newSubject", subject);
            model.addAttribute("exception", e);
            return "error/subject-add-error-page";
        }
        return "redirect:/subjects";
    }

    @PostMapping("/subjects/update/{id}")
    public String updateSubject(@ModelAttribute Subject subject, Model model) {
        try {
            subjectService.saveSubject(subject);
        } catch (ServiceException e) {
            model.addAttribute("subject", new Subject());
            model.addAttribute("newSubject", subject);
            model.addAttribute("exception", e);
            return "error/subject-edit-error-page";
        }
        return "redirect:/subjects";
    }
}
