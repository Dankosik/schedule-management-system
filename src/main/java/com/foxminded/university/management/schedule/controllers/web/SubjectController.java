package com.foxminded.university.management.schedule.controllers.web;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.SubjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class SubjectController {
    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
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
    public String addSubject(@Valid @ModelAttribute Subject subject, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnAdd", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("subjectWithErrors", new Subject(subject.getName(), subject.getLessons()));
            return "redirect:/subjects";
        }
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
    public String updateSubject(@Valid @ModelAttribute Subject subject, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnUpdate", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("subjectWithErrors", new Subject(subject.getId(), subject.getName(), subject.getLessons()));
            return "redirect:/subjects";
        }
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
