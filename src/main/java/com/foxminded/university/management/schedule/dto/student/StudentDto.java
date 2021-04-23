package com.foxminded.university.management.schedule.dto.student;

import com.foxminded.university.management.schedule.dto.group.GroupUpdateDto;

public interface StudentDto {
    String getFirstName();

    void setFirstName(String firstName);

    String getMiddleName();

    void setMiddleName(String middleName);

    String getLastName();

    void setLastName(String lastName);

    Integer getCourseNumber();

    void setCourseNumber(Integer courseNumber);

    GroupUpdateDto getGroup();

    void setGroup(GroupUpdateDto group);
}
