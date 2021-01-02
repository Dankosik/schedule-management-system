package com.foxminded.university.management.schedule.models;

import java.util.Objects;

public class University {
    private Long id;
    private Long scheduleId;

    public University() {
    }

    public University(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public University(Long id, Long scheduleId) {
        this.id = id;
        this.scheduleId = scheduleId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        University that = (University) o;
        return Objects.equals(id, that.id) && Objects.equals(scheduleId, that.scheduleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, scheduleId);
    }
}
