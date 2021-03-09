package com.foxminded.university.management.schedule.controllers;

import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("faculty", new Faculty());
        return "faculties";
    }

    @GetMapping("/faculties/{id}")
    public String showOneFaculty(@PathVariable("id") Long id, Model model) {
        Faculty faculty = facultyService.getFacultyById(id);
        model.addAttribute("faculty", faculty);

        model.addAttribute("groups", groupService.getGroupsForFaculty(faculty));
        model.addAttribute("teachers", teacherService.getTeachersForFaculty(faculty));

        model.addAttribute("group", new Group());
        model.addAttribute("teacher", new Teacher());
        model.addAttribute("allFaculties", facultyService.getAllFaculties());
        return "faculty";
    }

    @PostMapping("/faculties/delete/{id}")
    public String deleteFaculty(@PathVariable("id") Long id) {
        facultyService.deleteFacultyById(id);
        return "redirect:/faculties";
    }

    @PostMapping("/faculties/add")
    public String addFaculty(@ModelAttribute Faculty faculty, RedirectAttributes redirectAttributes) {
        try {
            facultyService.saveFaculty(faculty);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("newFaculty", faculty);
            redirectAttributes.addFlashAttribute("faculty", new Faculty());
            redirectAttributes.addFlashAttribute("serviceExceptionOnAdd", e);
            return "redirect:/faculties";
        }
        return "redirect:/faculties";
    }

    @PostMapping("/faculties/update/{id}")
    public String updateFaculty(@ModelAttribute Faculty faculty, RedirectAttributes redirectAttributes) {
        try {
            facultyService.saveFaculty(faculty);
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("newFaculty", faculty);
            redirectAttributes.addFlashAttribute("faculty", new Faculty());
            redirectAttributes.addFlashAttribute("serviceExceptionOnUpdate", e);
            return "redirect:/faculties";
        }
        return "redirect:/faculties";
    }
}
