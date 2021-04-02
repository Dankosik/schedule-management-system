package com.foxminded.university.management.schedule.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "audiences")
public class Audience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer number;
    private Integer capacity;
    @OneToMany(mappedBy = "audience", fetch = FetchType.LAZY)
    private List<Lecture> lectures;

    public Audience() {
    }

    public Audience(Long id, Integer number, Integer capacity, List<Lecture> lectures) {
        this.id = id;
        this.number = number;
        this.capacity = capacity;
        this.lectures = lectures;
    }

    public Audience(Integer number, Integer capacity, List<Lecture> lectures) {
        this.number = number;
        this.capacity = capacity;
        this.lectures = lectures;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audience audience = (Audience) o;
        return Objects.equals(number, audience.number) && Objects.equals(capacity, audience.capacity) &&
                Objects.equals(lectures, audience.lectures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, capacity, lectures);
    }

    @Override
    public String toString() {
        return "Audience{" +
                "id=" + id +
                ", number=" + number +
                ", capacity=" + capacity +
                '}';
    }
}
