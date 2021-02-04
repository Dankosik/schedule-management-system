package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.GroupService;
import com.foxminded.university.management.schedule.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class StudentController {
    private final StudentService studentService;
    private final GroupService groupService;

    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping("/students")
    public String showAllFaculties(Model model) {
        List<Student> students = studentService.getAllStudent();
        model.addAttribute("students", students);
        model.addAttribute("groupNames", groupService.getGroupNames(students));
        model.addAttribute("groups", groupService.getGroupsForStudents(students));
        return "students";
    }
}
