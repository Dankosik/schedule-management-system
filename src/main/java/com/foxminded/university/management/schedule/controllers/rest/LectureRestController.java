package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.documentation.LectureRestControllerDocumentation;
import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.lecture.BaseLectureDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureAddDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureUpdateDto;
import com.foxminded.university.management.schedule.dto.utils.LectureDtoUtils;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.LectureService;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/lectures")
public class LectureRestController implements LectureRestControllerDocumentation {
    private final LectureService lectureService;

    public LectureRestController(LectureService lectureService) {
        this.lectureService = lectureService;
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
    public ResponseEntity<Void> deleteLecture(@PathVariable("id") Long id) {
        lectureService.deleteLectureById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Lecture> addLecture(@Valid @RequestBody LectureAddDto lectureAddDto) {
        Lecture lecture = LectureDtoUtils.mapLectureDtoOnLecture(lectureAddDto);
        lectureService.saveLecture(lecture);
        return new ResponseEntity<>(lecture, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lecture> updateLecture(@Valid @RequestBody LectureUpdateDto lectureUpdateDto, @PathVariable("id") Long id) {
        if (!id.equals(lectureUpdateDto.getId())) {
            throw new UnacceptableUriException("URI id: " + id + " and request id: " +
                    lectureUpdateDto.getId() + " should be the same");
        }
        if (!lectureService.isLectureWithIdExist(id)) {
            throw new EntityNotFoundException("Lecture with id: " + id + " is not found");
        }

        Lecture lecture = LectureDtoUtils.mapLectureDtoOnLecture(lectureUpdateDto);
        lectureService.saveLecture(lecture);
        return ResponseEntity.ok(lecture);
    }
}
