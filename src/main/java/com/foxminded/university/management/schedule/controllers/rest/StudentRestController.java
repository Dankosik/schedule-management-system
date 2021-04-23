package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.utils.RestUtils;
import com.foxminded.university.management.schedule.dto.student.BaseStudentDto;
import com.foxminded.university.management.schedule.dto.student.StudentAddDto;
import com.foxminded.university.management.schedule.dto.student.StudentDto;
import com.foxminded.university.management.schedule.dto.student.StudentUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.StudentDtoUtils;
import com.foxminded.university.management.schedule.models.Student;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.StudentServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/students")
public class StudentRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentRestController.class);
    private final StudentServiceImpl studentService;
    private final GroupServiceImpl groupService;

    public StudentRestController(StudentServiceImpl studentService, GroupServiceImpl groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return new ResponseEntity<>(studentService.getAllStudents(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getStudentById(@PathVariable("id") Long id) {
        Student student = studentService.getStudentById(id);
        BaseStudentDto baseStudentDto = new BaseStudentDto();
        BeanUtils.copyProperties(student, baseStudentDto);
        return new ResponseEntity<>(baseStudentDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudentById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> addStudent(@Valid @RequestBody StudentAddDto studentAddDto) {
        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfStudentFieldsHasErrors(studentAddDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();
        Student student = StudentDtoUtils.mapStudentDtoOnStudent(studentAddDto);
        studentService.saveStudent(student);
        return new ResponseEntity<>(student, HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateStudent(@Valid @RequestBody StudentUpdateDto studentUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(studentUpdateDto.getId())) {
            return RestUtils.buildErrorResponseEntity("URI id: " + id + " and request id: " +
                    studentUpdateDto.getId() + " should be the same", HttpStatus.BAD_REQUEST);
        }

        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfStudentFieldsHasErrors(studentUpdateDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();

        if (!studentService.isStudentWithIdExist(id)) {
            LOGGER.warn("Student with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Student with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }

        Student student = StudentDtoUtils.mapStudentDtoOnStudent(studentUpdateDto);
        studentService.saveStudent(student);
        return ResponseEntity.ok(student);
    }

    private Optional<ResponseEntity<Object>> getErrorResponseEntityIfStudentFieldsHasErrors(StudentDto studentDto) {
        if (!groupService.isGroupWithIdExist(studentDto.getGroup().getId())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Group with id: " + studentDto.getGroup().getId() +
                    " is not found", HttpStatus.NOT_FOUND));
        }
        if (!StudentDtoUtils.isSuchGroupFromStudentDtoExist(studentDto))
            return Optional.of(RestUtils.buildErrorResponseEntity("Such group does not exist", HttpStatus.NOT_FOUND));

        return Optional.empty();
    }
}
