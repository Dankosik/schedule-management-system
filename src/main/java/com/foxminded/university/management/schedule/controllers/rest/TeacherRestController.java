package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.utils.RestUtils;
import com.foxminded.university.management.schedule.dto.teacher.BaseTeacherDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherAddDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.TeacherDtoUtils;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
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
@RequestMapping("api/v1/teachers")
public class TeacherRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherRestController.class);
    private final TeacherServiceImpl teacherService;
    private final FacultyServiceImpl facultyService;

    public TeacherRestController(TeacherServiceImpl teacherService, FacultyServiceImpl facultyService) {
        this.teacherService = teacherService;
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return new ResponseEntity<>(teacherService.getAllTeachers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTeacherById(@PathVariable("id") Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        BaseTeacherDto teacherDto = new BaseTeacherDto();
        BeanUtils.copyProperties(teacher, teacherDto);
        return new ResponseEntity<>(teacherDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTeacher(@PathVariable("id") Long id) {
        teacherService.deleteTeacherById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> addTeacher(@Valid @RequestBody TeacherAddDto teacherAddDto) {
        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfTeacherFieldsHasErrors(teacherAddDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();
        Teacher teacher = TeacherDtoUtils.mapTeacherDtoOnTeacher(teacherAddDto);
        teacherService.saveTeacher(teacher);
        return new ResponseEntity<>(teacher, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateTeacher(@Valid @RequestBody TeacherUpdateDto teacherUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(teacherUpdateDto.getId())) {
            return RestUtils.buildErrorResponseEntity("URI id: " + id + " and request id: " +
                    teacherUpdateDto.getId() + " should be the same", HttpStatus.BAD_REQUEST);
        }

        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfTeacherFieldsHasErrors(teacherUpdateDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();

        if (!teacherService.isTeacherWithIdExist(id)) {
            LOGGER.warn("Teacher with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Teacher with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }

        Teacher teacher = TeacherDtoUtils.mapTeacherDtoOnTeacher(teacherUpdateDto);
        teacherService.saveTeacher(teacher);
        return ResponseEntity.ok(teacher);
    }

    private Optional<ResponseEntity<Object>> getErrorResponseEntityIfTeacherFieldsHasErrors(TeacherDto teacherDto) {
        if (!facultyService.isFacultyWithIdExist(teacherDto.getFaculty().getId())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Faculty with id: " + teacherDto.getFaculty().getId() +
                    " is not found", HttpStatus.NOT_FOUND));
        }
        if (!TeacherDtoUtils.isSuchFacultyFromTeacherDtoExist(teacherDto))
            return Optional.of(RestUtils.buildErrorResponseEntity("Such faculty does not exist", HttpStatus.NOT_FOUND));

        return Optional.empty();
    }
}
