package com.foxminded.university.management.schedule.dto.subject;

import com.foxminded.university.management.schedule.models.Lesson;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class BaseSubjectDto {
    @Schema(description = "Unique identifier of the Subject.",
            example = "1", required = true)
    private Long id;
    @Schema(description = "Name of the subject.",
            example = "Math", required = true)
    private String name;
    private List<Lesson> lessons;

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

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
