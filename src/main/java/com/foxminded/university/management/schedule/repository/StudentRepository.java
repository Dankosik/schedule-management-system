package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Override
    @Query("select s from Student s left join fetch s.group g join fetch g.faculty")
    List<Student> findAll();
}
