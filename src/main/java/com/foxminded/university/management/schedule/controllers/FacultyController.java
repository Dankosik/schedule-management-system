package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FacultyController {
    private final FacultyServiceImpl facultyService;
    private final GroupServiceImpl groupService;
    private final TeacherServiceImpl teacherService;

    public FacultyController(FacultyServiceImpl facultyService, GroupServiceImpl groupService,
                             TeacherServiceImpl teacherService) {
        this.facultyService = facultyService;
        this.groupService = groupService;
        this.teacherService = teacherService;
    }

    @GetMapping("/faculties")
    public String showAllFaculties(Model model) {
        model.addAttribute("faculties", facultyService.getAllFaculties());
        return "faculties";
    }

    @GetMapping("/faculties/{id}")
    public String showOneFaculty(@PathVariable("id") Long id, Model model) {
        Faculty faculty = facultyService.getFacultyById(id);
        model.addAttribute("faculty", faculty);

        model.addAttribute("groups", groupService.getGroupsForFaculty(faculty));
        model.addAttribute("teachers", teacherService.getTeachersForFaculty(faculty));
        return "faculty";
    }
}
