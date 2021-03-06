package com.foxminded.university.management.schedule.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers")
public class Teacher extends Person {
    @Schema(description = "Unique identifier of the Teacher.",
            example = "1", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Faculty faculty;
    @JsonIgnore
    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<Lecture> lectures;

    public Teacher() {

    }

    public Teacher(Long id, String firstName, String lastName, String middleName, Faculty faculty, List<Lecture> lectures) {
        super(firstName, lastName, middleName);
        this.id = id;
        this.faculty = faculty;
        this.lectures = lectures;
    }

    public Teacher(String firstName, String lastName, String middleName, Faculty faculty, List<Lecture> lectures) {
        super(firstName, lastName, middleName);
        this.faculty = faculty;
        this.lectures = lectures;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
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
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(faculty, teacher.faculty) && Objects.equals(lectures, teacher.lectures);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), faculty, lectures);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", faculty=" + faculty +
                '}';
    }
}
