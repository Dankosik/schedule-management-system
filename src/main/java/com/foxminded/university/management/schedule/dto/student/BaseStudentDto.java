package com.foxminded.university.management.schedule.dto.student;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.validators.HumanName;
import io.swagger.v3.oas.annotations.media.Schema;

public class BaseStudentDto {
    @Schema(description = "Unique identifier of the Student.",
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
    @Schema(description = "Course number of the student.",
            example = "2", required = true)
    private Integer courseNumber;
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Group group;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
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
