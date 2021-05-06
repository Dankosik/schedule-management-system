package com.foxminded.university.management.schedule.controllers.rest;

import com.foxminded.university.management.schedule.controllers.rest.documentation.FacultyRestControllerDocumentation;
import com.foxminded.university.management.schedule.controllers.rest.exceptions.UnacceptableUriException;
import com.foxminded.university.management.schedule.dto.faculty.BaseFacultyDto;
import com.foxminded.university.management.schedule.dto.faculty.FacultyAddDto;
import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.service.FacultyService;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/faculties")
public class FacultyRestController implements FacultyRestControllerDocumentation {
    private final FacultyService facultyService;

    public FacultyRestController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> getAllFaculties() {
        return new ResponseEntity<>(facultyService.getAllFaculties(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseFacultyDto> getFacultyById(@PathVariable("id") Long id) {
        Faculty faculty = facultyService.getFacultyById(id);
        BaseFacultyDto facultyDto = new BaseFacultyDto();
        BeanUtils.copyProperties(faculty, facultyDto);
        return ResponseEntity.ok(facultyDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable("id") Long id) {
        facultyService.deleteFacultyById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Faculty> addFaculty(@Valid @RequestBody FacultyAddDto facultyAddDto) {
        Faculty faculty = new Faculty();
        faculty.setName(facultyAddDto.getName());
        facultyService.saveFaculty(faculty);
        return new ResponseEntity<>(faculty, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Faculty> updateFaculty(@Valid @RequestBody FacultyUpdateDto facultyUpdateDto, @PathVariable("id") Long id) {
        Faculty faculty = new Faculty();
        BeanUtils.copyProperties(facultyUpdateDto, faculty);

        if (!id.equals(facultyUpdateDto.getId())) {
            throw new UnacceptableUriException("URI id: " + id + " and request id: " +
                    facultyUpdateDto.getId() + " should be the same");
        }
        if (!facultyService.isFacultyWithIdExist(id)) {
            throw new EntityNotFoundException("Faculty with id: " + id + " is not found");
        }

        facultyService.saveFaculty(faculty);
        return ResponseEntity.ok(faculty);
    }
}
