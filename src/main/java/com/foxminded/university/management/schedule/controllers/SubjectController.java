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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String addSubject(@ModelAttribute Subject subject, RedirectAttributes redirectAttributes) {
        try {
            subjectService.saveSubject(subject);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("subject", new Subject());
            redirectAttributes.addFlashAttribute("newSubject", subject);
            redirectAttributes.addFlashAttribute("serviceExceptionOnAdd", e);
            return "redirect:/subjects";
        }
        return "redirect:/subjects";
    }

    @PostMapping("/subjects/update/{id}")
    public String updateSubject(@ModelAttribute Subject subject, RedirectAttributes redirectAttributes) {
        try {
            subjectService.saveSubject(subject);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("subject", new Subject());
            redirectAttributes.addFlashAttribute("newSubject", subject);
            redirectAttributes.addFlashAttribute("serviceExceptionOnUpdate", e);
            return "redirect:/subjects";
        }
        return "redirect:/subjects";
    }
}
