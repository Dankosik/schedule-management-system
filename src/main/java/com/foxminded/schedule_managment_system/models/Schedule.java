package com.foxminded.schedule_managment_system.models;

import java.util.List;
import java.util.Objects;

public class Schedule {
    private List<Lecture> lectures;

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }

    public void addLecture(Lecture lecture) {
        this.lectures.add(lecture);
    }

    public void removeLecture(Lecture lecture) {
        this.lectures.remove(lecture);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Schedule schedule = (Schedule) o;
        return Objects.equals(lectures, schedule.lectures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lectures);
    }
}
