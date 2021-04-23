package com.foxminded.university.management.schedule.dto.audience;

import com.foxminded.university.management.schedule.models.Lecture;

import java.util.List;

public class BaseAudienceDto {
    private Long id;
    private Integer number;
    private Integer capacity;
    private List<Lecture> lectures;

    public BaseAudienceDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
