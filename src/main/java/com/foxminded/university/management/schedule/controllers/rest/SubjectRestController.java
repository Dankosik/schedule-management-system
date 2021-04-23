package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.utils.RestUtils;
import com.foxminded.university.management.schedule.dto.subject.BaseSubjectDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectAddDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/subjects")
public class SubjectRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectRestController.class);
    private final SubjectServiceImpl subjectService;

    public SubjectRestController(SubjectServiceImpl subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return new ResponseEntity<>(subjectService.getAllSubjects(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getSubjectById(@PathVariable("id") Long id) {
        Subject subject = subjectService.getSubjectById(id);
        BaseSubjectDto subjectDto = new BaseSubjectDto();
        BeanUtils.copyProperties(subject, subjectDto);
        return ResponseEntity.ok(subjectDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSubject(@PathVariable("id") Long id) {
        subjectService.deleteSubjectById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> addSubject(@Valid @RequestBody SubjectAddDto subjectAddDto) {
        Subject subject = new Subject();
        subject.setName(subjectAddDto.getName());
        subjectService.saveSubject(subject);
        return new ResponseEntity<>(subject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateSubject(@Valid @RequestBody SubjectUpdateDto subjectUpdateDto, @PathVariable("id") Long id) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectUpdateDto, subject);

        if (!id.equals(subjectUpdateDto.getId())) {
            return RestUtils.buildErrorResponseEntity("URI id: " + id + " and request id: " +
                    subjectUpdateDto.getId() + " should be the same", HttpStatus.BAD_REQUEST);
        }

        if (!subjectService.isSubjectWithIdExist(id)) {
            LOGGER.warn("Subject with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Subject with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }

        subjectService.saveSubject(subject);
        return ResponseEntity.ok(subject);
    }
}
