package com.foxminded.university.management.schedule.dto.student;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;
import com.foxminded.university.management.schedule.models.validators.HumanName;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class StudentAddDto implements StudentDto {
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
    @Schema(description = "Course number of the student.",
            example = "2", required = true)
    @Min(value = 1, message = "Must be at least 1")
    @Max(value = 4, message = "Must be no more than 4")
    private Integer courseNumber;
    @NotNull
    private GroupUpdateDto group;

    @JsonCreator
    public StudentAddDto(@JsonProperty(required = true) String firstName, @JsonProperty(required = true) String middleName,
                         @JsonProperty(required = true) String lastName, @JsonProperty(required = true) Integer courseNumber,
                         @JsonProperty(required = true) GroupUpdateDto group) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.courseNumber = courseNumber;
        this.group = group;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getMiddleName() {
        return middleName;
    }

    @Override
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public Integer getCourseNumber() {
        return courseNumber;
    }

    @Override
    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    @Override
    public GroupUpdateDto getGroup() {
        return group;
    }

    public void setGroup(GroupUpdateDto group) {
        this.group = group;
    }
}
