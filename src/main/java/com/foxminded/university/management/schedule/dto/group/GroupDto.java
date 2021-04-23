package com.foxminded.university.management.schedule.dto.group;

import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;

public interface GroupDto {
    String getName();

    void setName(String name);

    FacultyUpdateDto getFaculty();

    void setFaculty(FacultyUpdateDto faculty);
}
