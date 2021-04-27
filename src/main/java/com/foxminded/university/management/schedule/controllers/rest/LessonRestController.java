package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.lesson.BaseLessonDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonAddDto;
import com.foxminded.university.management.schedule.dto.lesson.LessonUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.LessonDtoUtils;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/lessons")
public class LessonRestController {
    private final LessonServiceImpl lessonService;

    public LessonRestController(LessonServiceImpl lessonService) {
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<Lesson>> getAllLessons() {
        return new ResponseEntity<>(lessonService.getAllLessons(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseLessonDto> getLessonById(@PathVariable("id") Long id) {
        Lesson lesson = lessonService.getLessonById(id);
        BaseLessonDto baseLessonDto = new BaseLessonDto();
        BeanUtils.copyProperties(lesson, baseLessonDto);
        return new ResponseEntity<>(baseLessonDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLesson(@PathVariable("id") Long id) {
        lessonService.deleteLessonById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Lesson> addLesson(@Valid @RequestBody LessonAddDto lessonAddDto) {
        Lesson lesson = LessonDtoUtils.mapLessonDtoOnLesson(lessonAddDto);
        lessonService.saveLesson(lesson);
        return new ResponseEntity<>(lesson, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lesson> updateLesson(@Valid @RequestBody LessonUpdateDto lessonUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(lessonUpdateDto.getId())) {
            throw new UnacceptableUriException("URI id: " + id + " and request id: " +
                    lessonUpdateDto.getId() + " should be the same");
        }
        if (!lessonService.isLessonWithIdExist(id)) {
            throw new EntityNotFoundException("Lesson with id: " + id + " is not found");
        }

        Lesson lesson = LessonDtoUtils.mapLessonDtoOnLesson(lessonUpdateDto);
        lessonService.saveLesson(lesson);
        return ResponseEntity.ok(lesson);
    }
}
