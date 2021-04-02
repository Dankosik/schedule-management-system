package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Override
    @Query("select lecture from Lecture lecture " +
            "left join fetch lecture.group g left join fetch g.faculty left join fetch lecture.audience left join fetch lecture.lesson le " +
            "left join fetch le.subject left join fetch lecture.teacher t left join fetch t.faculty")
    List<Lecture> findAll();
}
