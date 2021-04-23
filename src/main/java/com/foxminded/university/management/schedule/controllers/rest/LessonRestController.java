package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.utils.RestUtils;
import com.foxminded.university.management.schedule.dto.lesson.BaseLessonDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonAddDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.LessonDtoUtils;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
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
@RequestMapping("api/v1/lessons")
public class LessonRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LessonRestController.class);
    private final LessonServiceImpl lessonService;
    private final SubjectServiceImpl subjectService;

    public LessonRestController(LessonServiceImpl lessonService, SubjectServiceImpl subjectService) {
        this.lessonService = lessonService;
        this.subjectService = subjectService;
    }

    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        return new ResponseEntity<>(lessonService.getAllLessons(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getLessonById(@PathVariable("id") Long id) {
        Lesson lesson;
        try {
            lesson = lessonService.getLessonById(id);
        } catch (ServiceException e) {
            LOGGER.warn("Lesson with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Lesson with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }
        BaseLessonDto baseLessonDto = new BaseLessonDto();
        BeanUtils.copyProperties(lesson, baseLessonDto);
        return new ResponseEntity<>(baseLessonDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLesson(@PathVariable("id") Long id) {
        if (!lessonService.isLessonWithIdExist(id)) {
            LOGGER.warn("Lesson with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Lesson with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }
        lessonService.deleteLessonById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> addLesson(@Valid @RequestBody LessonAddDto lessonAddDto) {
        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfLessonFieldsHasErrors(lessonAddDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();
        Lesson lesson = LessonDtoUtils.mapLessonDtoOnLesson(lessonAddDto);
        lessonService.saveLesson(lesson);
        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLesson(@Valid @RequestBody LessonUpdateDto lessonUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(lessonUpdateDto.getId())) {
            return RestUtils.buildErrorResponseEntity("URI id: " + id + " and request id: " +
                    lessonUpdateDto.getId() + " should be the same", HttpStatus.BAD_REQUEST);
        }
        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfLessonFieldsHasErrors(lessonUpdateDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();
        if (!lessonService.isLessonWithIdExist(id)) {
            LOGGER.warn("Lesson with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Lesson with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }
        Lesson lesson = LessonDtoUtils.mapLessonDtoOnLesson(lessonUpdateDto);
        lessonService.saveLesson(lesson);
        return ResponseEntity.ok(lesson);
    }

    private Optional<ResponseEntity<Object>> getErrorResponseEntityIfLessonFieldsHasErrors(LessonDto lessonDto) {
        if (!subjectService.isSubjectWithIdExist(lessonDto.getSubject().getId())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Subject with id: " + lessonDto.getSubject().getId() +
                    " is not found", HttpStatus.NOT_FOUND));
        }
        if (!LessonDtoUtils.isSuchSubjectFromLessonDtoExist(lessonDto))
            return Optional.of(RestUtils.buildErrorResponseEntity("Such subject does not exist", HttpStatus.NOT_FOUND));
        return Optional.empty();
    }
}
