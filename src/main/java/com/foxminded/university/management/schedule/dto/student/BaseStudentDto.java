package com.foxminded.university.management.schedule.dto.student;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.foxminded.university.management.schedule.models.Group;
import com.foxminded.university.management.schedule.models.validators.HumanName;

public class BaseStudentDto {
    private Long id;
    @HumanName
    private String firstName;
    @HumanName
    private String lastName;
    @HumanName
    private String middleName;
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
