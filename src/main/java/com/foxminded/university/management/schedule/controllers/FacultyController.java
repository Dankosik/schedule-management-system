package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FacultyController {
    private final FacultyServiceImpl facultyService;

    public FacultyController(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("/faculties")
    public String showAllFaculties(Model model) {
        model.addAttribute("faculties", facultyService.getAllFaculties());
        return "faculties";
    }

    @GetMapping("/faculties/{id}")
    public String showOneTeacher(@PathVariable("id") Long id, Model model) {
        model.addAttribute("faculty", facultyService.getFacultyById(id));
        return "faculty";
    }
}
