package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.utils.RestUtils;
import com.foxminded.university.management.schedule.dto.lecture.BaseLectureDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureAddDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.LectureDtoUtils;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.impl.*;
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
@RequestMapping("api/v1/lectures")
public class LectureRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LectureRestController.class);
    private final LectureServiceImpl lectureService;
    private final AudienceServiceImpl audienceService;
    private final TeacherServiceImpl teacherService;
    private final GroupServiceImpl groupService;
    private final LessonServiceImpl lessonService;

    public LectureRestController(LectureServiceImpl lectureService, AudienceServiceImpl audienceService,
                                 TeacherServiceImpl teacherService, GroupServiceImpl groupService, LessonServiceImpl lessonService) {
        this.lectureService = lectureService;
        this.audienceService = audienceService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.lessonService = lessonService;
    }

    @GetMapping
    public ResponseEntity<List<Lecture>> getAllLectures() {
        return new ResponseEntity<>(lectureService.getAllLectures(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseLectureDto> getLectureById(@PathVariable("id") Long id) {
        Lecture lecture = lectureService.getLectureById(id);
        BaseLectureDto baseLectureDto = new BaseLectureDto();
        BeanUtils.copyProperties(lecture, baseLectureDto);
        return new ResponseEntity<>(baseLectureDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLecture(@PathVariable("id") Long id) {
        lectureService.deleteLectureById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> addLecture(@Valid @RequestBody LectureAddDto lectureAddDto) {
        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfLectureFieldsHasErrors(lectureAddDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();
        Lecture lecture = LectureDtoUtils.mapLectureDtoOnLecture(lectureAddDto);
        lectureService.saveLecture(lecture);
        return new ResponseEntity<>(lecture, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateLecture(@Valid @RequestBody LectureUpdateDto lectureUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(lectureUpdateDto.getId())) {
            return RestUtils.buildErrorResponseEntity("URI id: " + id + " and request id: " +
                    lectureUpdateDto.getId() + " should be the same", HttpStatus.BAD_REQUEST);
        }

        Optional<ResponseEntity<Object>> errorResponseEntity = getErrorResponseEntityIfLectureFieldsHasErrors(lectureUpdateDto);
        if (errorResponseEntity.isPresent()) return errorResponseEntity.get();

        if (!lectureService.isLectureWithIdExist(id)) {
            LOGGER.warn("Lecture with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Lecture with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }

        Lecture lecture = LectureDtoUtils.mapLectureDtoOnLecture(lectureUpdateDto);
        lectureService.saveLecture(lecture);
        return ResponseEntity.ok(lecture);
    }

    private Optional<ResponseEntity<Object>> getErrorResponseEntityIfLectureFieldsHasErrors(LectureDto lectureDto) {
        if (!lectureDto.getNumber().equals(lectureDto.getLesson().getNumber())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Lecture number [" + lectureDto.getNumber() +
                    "] and lesson number [" + lectureDto.getLesson().getNumber() + "] should be equal", HttpStatus.BAD_REQUEST));
        }

        if (!audienceService.isAudienceWithIdExist(lectureDto.getAudience().getId())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Audience with id: " + lectureDto.getAudience().getId() +
                    " is not found", HttpStatus.NOT_FOUND));
        }
        if (!LectureDtoUtils.isSuchAudienceFromLectureDtoExist(lectureDto))
            return Optional.of(RestUtils.buildErrorResponseEntity("Such audience does not exist", HttpStatus.NOT_FOUND));

        if (!teacherService.isTeacherWithIdExist(lectureDto.getTeacher().getId())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Teacher with id: " + lectureDto.getTeacher().getId() +
                    " is not found", HttpStatus.NOT_FOUND));
        }
        if (!LectureDtoUtils.isSuchTeacherFromLectureDtoExist(lectureDto))
            return Optional.of(RestUtils.buildErrorResponseEntity("Such teacher does not exist", HttpStatus.NOT_FOUND));

        if (!groupService.isGroupWithIdExist(lectureDto.getGroup().getId())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Group with id: " + lectureDto.getGroup().getId() +
                    " is not found", HttpStatus.NOT_FOUND));
        }
        if (!LectureDtoUtils.isSuchGroupFromLectureDtoExist(lectureDto))
            return Optional.of(RestUtils.buildErrorResponseEntity("Such group does not exist", HttpStatus.NOT_FOUND));

        if (!lessonService.isLessonWithIdExist(lectureDto.getLesson().getId())) {
            return Optional.of(RestUtils.buildErrorResponseEntity("Lesson with id: " + lectureDto.getLesson().getId() +
                    " is not found", HttpStatus.NOT_FOUND));
        }
        if (!LectureDtoUtils.isSuchLessonFromLectureDtoExist(lectureDto))
            return Optional.of(RestUtils.buildErrorResponseEntity("Such lesson does not exist", HttpStatus.NOT_FOUND));
        return Optional.empty();
    }
}
