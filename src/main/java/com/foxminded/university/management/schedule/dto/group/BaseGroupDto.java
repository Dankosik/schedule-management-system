package com.foxminded.university.management.schedule.dto.group;

import com.foxminded.university.management.schedule.models.Faculty;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.models.Student;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class BaseGroupDto {
    @Schema(description = "Unique identifier of the Group.",
            example = "1", required = true)
    private Long id;
    @Schema(description = "Name of the group.",
            example = "AB-01", required = true)
    private String name;
    private Faculty faculty;
    private List<Student> students;
    private List<Lecture> lectures;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
