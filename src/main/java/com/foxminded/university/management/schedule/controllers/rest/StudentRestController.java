package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.student.BaseStudentDto;
import com.foxminded.university.management.schedule.dto.student.StudentAddDto;
import com.foxminded.university.management.schedule.dto.student.StudentUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.StudentDtoUtils;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.StudentServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentRestController {
    private final StudentServiceImpl studentService;

    public StudentRestController(StudentServiceImpl studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseStudentDto> getStudentById(@PathVariable("id") Long id) {
        Student student = studentService.getStudentById(id);
        BaseStudentDto baseStudentDto = new BaseStudentDto();
        BeanUtils.copyProperties(student, baseStudentDto);
        return new ResponseEntity<>(baseStudentDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@Valid @RequestBody StudentAddDto studentAddDto) {
        Student student = StudentDtoUtils.mapStudentDtoOnStudent(studentAddDto);
        studentService.saveStudent(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@Valid @RequestBody StudentUpdateDto studentUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(studentUpdateDto.getId())) {
            throw new UnacceptableUriException("URI id: " + id + " and request id: " +
                    studentUpdateDto.getId() + " should be the same");
        }

        if (!studentService.isStudentWithIdExist(id)) {
            throw new EntityNotFoundException("Student with id: " + id + " is not found");
        }

        Student student = StudentDtoUtils.mapStudentDtoOnStudent(studentUpdateDto);
        studentService.saveStudent(student);
        return ResponseEntity.ok(student);
    }
}
