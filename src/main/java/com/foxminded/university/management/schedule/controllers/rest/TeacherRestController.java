package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.documentation.TeacherRestControllerDocumentation;
import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.teacher.BaseTeacherDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherAddDto;
import com.foxminded.university.management.schedule.dto.teacher.TeacherUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.TeacherDtoUtils;
import com.foxminded.university.management.schedule.models.Teacher;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/teachers")
public class TeacherRestController implements TeacherRestControllerDocumentation {
    private final TeacherServiceImpl teacherService;

    public TeacherRestController(TeacherServiceImpl teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public ResponseEntity<List<Teacher>> getAllTeachers() {
        return new ResponseEntity<>(teacherService.getAllTeachers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseTeacherDto> getTeacherById(@PathVariable("id") Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        BaseTeacherDto teacherDto = new BaseTeacherDto();
        BeanUtils.copyProperties(teacher, teacherDto);
        return new ResponseEntity<>(teacherDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeacher(@PathVariable("id") Long id) {
        teacherService.deleteTeacherById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Teacher> addTeacher(@Valid @RequestBody TeacherAddDto teacherAddDto) {
        Teacher teacher = TeacherDtoUtils.mapTeacherDtoOnTeacher(teacherAddDto);
        teacherService.saveTeacher(teacher);
        return new ResponseEntity<>(teacher, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Teacher> updateTeacher(@Valid @RequestBody TeacherUpdateDto teacherUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(teacherUpdateDto.getId())) {
            throw new UnacceptableUriException("URI id: " + id + " and request id: " +
                    teacherUpdateDto.getId() + " should be the same");
        }
        if (!teacherService.isTeacherWithIdExist(id)) {
            throw new EntityNotFoundException("Teacher with id: " + id + " is not found");
        }

        Teacher teacher = TeacherDtoUtils.mapTeacherDtoOnTeacher(teacherUpdateDto);
        teacherService.saveTeacher(teacher);
        return ResponseEntity.ok(teacher);
    }
}
