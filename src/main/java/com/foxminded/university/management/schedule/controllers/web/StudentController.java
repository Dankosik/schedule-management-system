package com.foxminded.university.management.schedule.controllers.web;

import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.GroupService;
import com.foxminded.university.management.schedule.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
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
    public String showAllStudents(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        model.addAttribute("groupNames", groupService.getGroupNamesWithPossibleNullForStudents(students));
        model.addAttribute("groups", groupService.getGroupsWithPossibleNullForStudents(students));
        model.addAttribute("allGroups", groupService.getAllGroups());
        model.addAttribute("student", new Student());
        return "students";
    }

    @PostMapping("/students/delete/{id}")
    public String deleteAudience(@PathVariable("id") Long id) {
        studentService.deleteStudentById(id);
        return "redirect:/students";
    }

    @PostMapping("/students/add")
    public String addStudent(@Valid @ModelAttribute Student student, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnAdd", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("studentWithErrors",
                    new Student(student.getFirstName(), student.getLastName(), student.getMiddleName(), student.getCourseNumber(), student.getGroup()));
            return "redirect:/students";
        }
        studentService.saveStudent(student);
        return "redirect:/students";
    }

    @PostMapping("/students/update/{id}")
    public String updateStudent(@Valid @ModelAttribute Student student, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("fieldErrorsOnUpdate", bindingResult.getFieldErrors());
            redirectAttributes.addFlashAttribute("studentWithErrors",
                    new Student(student.getId(), student.getFirstName(), student.getLastName(), student.getMiddleName(),
                            student.getCourseNumber(), student.getGroup()));
            return "redirect:/students";
        }
        studentService.saveStudent(student);
        return "redirect:/students";
    }
}
