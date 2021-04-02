package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Faculty;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    @Override
    @EntityGraph(attributePaths = "groups")
    Optional<Faculty> findById(Long id);
}
