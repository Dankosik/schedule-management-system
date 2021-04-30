package com.foxminded.university.management.schedule.dto.teacher;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.validators.HumanName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class BaseTeacherDto {
    @Schema(description = "Unique identifier of the Teacher.",
            example = "1", required = true)
    private Long id;
    @Schema(description = "First name of the person.",
            example = "John", required = true)
    @HumanName
    private String firstName;
    @Schema(description = "Last name of the person.",
            example = "Williams", required = true)
    @HumanName
    private String lastName;
    @Schema(description = "Middle name name of the person.",
            example = "Williams", required = true)
    @HumanName
    private String middleName;
    private Faculty faculty;
    private List<Lecture> lectures;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
}
