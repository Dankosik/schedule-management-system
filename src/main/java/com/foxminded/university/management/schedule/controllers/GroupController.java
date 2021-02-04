package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.service.GroupService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    public String showAllFaculties(Model model) {
        model.addAttribute("groups", groupService.getAllAGroups());
        return "groups";
    }

    @GetMapping("/groups/{id}")
    public String showOneAudience(@PathVariable("id") Long id, Model model) {
        model.addAttribute("group", groupService.getGroupById(id));
        return "group";
    }
}
