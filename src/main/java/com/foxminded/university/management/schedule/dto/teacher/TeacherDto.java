package com.foxminded.university.management.schedule.dto.teacher;

import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;

public interface TeacherDto {
    String getFirstName();

    void setFirstName(String firstName);

    String getMiddleName();

    void setMiddleName(String middleName);

    String getLastName();

    void setLastName(String lastName);

    FacultyUpdateDto getFaculty();

    void setFaculty(FacultyUpdateDto faculty);
}
