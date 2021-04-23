package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.utils.RestUtils;
import com.foxminded.university.management.schedule.dto.faculty.BaseFacultyDto;
import com.foxminded.university.management.schedule.dto.faculty.FacultyAddDto;
import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.service.impl.FacultyServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/faculties")
public class FacultyRestController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FacultyRestController.class);
    private final FacultyServiceImpl facultyService;

    public FacultyRestController(FacultyServiceImpl facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        return new ResponseEntity<>(facultyService.getAllFaculties(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getFacultyById(@PathVariable("id") Long id) {
        Faculty faculty = facultyService.getFacultyById(id);
        BaseFacultyDto facultyDto = new BaseFacultyDto();
        BeanUtils.copyProperties(faculty, facultyDto);
        return ResponseEntity.ok(facultyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFaculty(@PathVariable("id") Long id) {
        facultyService.deleteFacultyById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<Object> addFaculty(@Valid @RequestBody FacultyAddDto facultyAddDto) {
        Faculty faculty = new Faculty();
        faculty.setName(facultyAddDto.getName());
        facultyService.saveFaculty(faculty);
        return new ResponseEntity<>(faculty, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateAudience(@Valid @RequestBody FacultyUpdateDto facultyUpdateDto, @PathVariable("id") Long id) {
        Faculty faculty = new Faculty();
        BeanUtils.copyProperties(facultyUpdateDto, faculty);

        if (!id.equals(facultyUpdateDto.getId())) {
            return RestUtils.buildErrorResponseEntity("URI id: " + id + " and request id: " +
                    facultyUpdateDto.getId() + " should be the same", HttpStatus.BAD_REQUEST);
        }

        if (!facultyService.isFacultyWithIdExist(id)) {
            LOGGER.warn("Faculty with id: {} is not found", id);
            return RestUtils.buildErrorResponseEntity("Faculty with id: " + id + " is not found", HttpStatus.NOT_FOUND);
        }

        facultyService.saveFaculty(faculty);
        return ResponseEntity.ok(faculty);
    }
}
