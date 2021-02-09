package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.StudentServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentController {
    private final StudentServiceImpl studentService;
    private final GroupServiceImpl groupService;

    public StudentController(StudentServiceImpl studentService, GroupServiceImpl groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("/students")
    public String showAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("groupNames", groupService.getGroupNamesForStudents(students));
        model.addAttribute("groups", groupService.getGroupsForStudents(students));
        return "students";
    }
}
