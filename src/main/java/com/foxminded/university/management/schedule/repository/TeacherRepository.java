package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Override
    @Query("select t from Teacher t left join fetch t.faculty left join fetch t.lectures l " +
            "left join fetch l.group g left join fetch g.faculty left join fetch l.audience left join fetch l.lesson le " +
            "left join fetch le.subject where t.id =:id")
    Optional<Teacher> findById(@Param("id") Long id);

    @Override
    @EntityGraph(attributePaths = "faculty")
    List<Teacher> findAll();
}
