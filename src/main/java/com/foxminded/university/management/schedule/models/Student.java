package com.foxminded.university.management.schedule.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Min(value = 1, message = "Must be at least 1")
    @Max(value = 4, message = "Must be no more than 4")
    private Integer courseNumber;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Group group;

    public Student() {
    }

    public Student(Long id, String firstName, String lastName, String middleName, Integer courseNumber, Group group) {
        super(firstName, lastName, middleName);
        this.id = id;
        this.courseNumber = courseNumber;
        this.group = group;
    }

    public Student(String firstName, String lastName, String middleName, Integer courseNumber, Group group) {
        super(firstName, lastName, middleName);
        this.courseNumber = courseNumber;
        this.group = group;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Integer getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(Integer courseNumber) {
        this.courseNumber = courseNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(courseNumber, student.courseNumber) && Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), courseNumber, group);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", courseNumber=" + courseNumber +
                ", group=" + group +
                '}';
    }
}
