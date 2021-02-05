package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TeacherController {
    private final TeacherServiceImpl teacherService;
    private final FacultyServiceImpl facultyService;

    public TeacherController(TeacherServiceImpl teacherService, FacultyServiceImpl facultyService) {
        this.teacherService = teacherService;
        this.facultyService = facultyService;
    }

    @GetMapping("/teachers")
    public String showAllFaculties(Model model) {
        List<Teacher> teachers = teacherService.getAllTeachers();
        model.addAttribute("teachers", teachers);
        model.addAttribute("facultyNames", facultyService.getFacultyNamesForTeachers(teachers));
        model.addAttribute("faculties", facultyService.getFacultiesForTeachers(teachers));
        return "teachers";
    }

    @GetMapping("/teachers/{id}")
    public String showOneTeacher(@PathVariable("id") Long id, Model model) {
        Teacher teacher = teacherService.getTeacherById(id);
        model.addAttribute("teacher", teacher);
        model.addAttribute("faculty", facultyService.getFacultyById(teacher.getFacultyId()));
        return "teacher";
    }
}
