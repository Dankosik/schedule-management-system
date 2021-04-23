package com.foxminded.university.management.schedule.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foxminded.university.management.schedule.models.validators.SubjectName;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "subjects")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @SubjectName
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "subject", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    public Subject() {

    }

    public Subject(Long id, String name, List<Lesson> lessons) {
        this.id = id;
        this.name = name;
        this.lessons = lessons;
    }

    public Subject(String name, List<Lesson> lessons) {
        this.name = name;
        this.lessons = lessons;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(name, subject.name) && Objects.equals(lessons, subject.lessons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lessons);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
