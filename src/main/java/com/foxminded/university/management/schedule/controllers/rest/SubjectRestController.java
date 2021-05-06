package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.documentation.SubjectRestControllerDocumentation;
import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.subject.BaseSubjectDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectAddDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.SubjectService;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/subjects")
public class SubjectRestController implements SubjectRestControllerDocumentation {
    private final SubjectService subjectService;

    public SubjectRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<Subject>> getAllSubjects() {
        return new ResponseEntity<>(subjectService.getAllSubjects(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseSubjectDto> getSubjectById(@PathVariable("id") Long id) {
        Subject subject = subjectService.getSubjectById(id);
        BaseSubjectDto subjectDto = new BaseSubjectDto();
        BeanUtils.copyProperties(subject, subjectDto);
        return ResponseEntity.ok(subjectDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSubject(@PathVariable("id") Long id) {
        subjectService.deleteSubjectById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Subject> addSubject(@Valid @RequestBody SubjectAddDto subjectAddDto) {
        Subject subject = new Subject();
        subject.setName(subjectAddDto.getName());
        subjectService.saveSubject(subject);
        return new ResponseEntity<>(subject, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Subject> updateSubject(@Valid @RequestBody SubjectUpdateDto subjectUpdateDto, @PathVariable("id") Long id) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(subjectUpdateDto, subject);

        if (!id.equals(subjectUpdateDto.getId())) {
            throw new UnacceptableUriException("URI id: " + id + " and request id: " +
                    subjectUpdateDto.getId() + " should be the same");
        }
        if (!subjectService.isSubjectWithIdExist(id)) {
            throw new EntityNotFoundException("Subject with id: " + id + " is not found");
        }

        subjectService.saveSubject(subject);
        return ResponseEntity.ok(subject);
    }
}
