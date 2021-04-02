package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Lesson;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    @Override
    @EntityGraph(attributePaths = "subject")
    Optional<Lesson> findById(Long id);

    @Override
    @EntityGraph(attributePaths = "subject")
    List<Lesson> findAll();
}
