package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.StudentServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GroupController {
    private final GroupServiceImpl groupService;
    private final StudentServiceImpl studentService;

    public GroupController(GroupServiceImpl groupService, StudentServiceImpl studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @GetMapping("/groups")
    public String showAllGroups(Model model) {
        model.addAttribute("groups", groupService.getAllAGroups());
        return "groups";
    }

    @GetMapping("/groups/{id}")
    public String showOneGroup(@PathVariable("id") Long id, Model model) {
        Group group = groupService.getGroupById(id);
        model.addAttribute("group", group);
        model.addAttribute("students", studentService.getStudentsForGroup(group));
        return "group";
    }
}
