package com.foxminded.university.management.schedule.dto.teacher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.models.validators.HumanName;

import javax.validation.constraints.NotNull;

public class TeacherAddDto implements TeacherDto {
    @HumanName
    private String firstName;
    @HumanName
    private String lastName;
    @HumanName
    private String middleName;
    @NotNull
    private FacultyUpdateDto faculty;

    @JsonCreator
    public TeacherAddDto(@JsonProperty(required = true) String firstName,
                         @JsonProperty(required = true) String lastName, @JsonProperty(required = true) String middleName,
                         @JsonProperty(required = true) FacultyUpdateDto faculty) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.faculty = faculty;
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

    public FacultyUpdateDto getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyUpdateDto faculty) {
        this.faculty = faculty;
    }
}
