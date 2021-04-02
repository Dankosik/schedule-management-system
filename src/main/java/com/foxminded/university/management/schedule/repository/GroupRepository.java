package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Override
    @EntityGraph(attributePaths = {"faculty"})
    List<Group> findAll();

    @Override
    @Query("select g from Group g left join fetch g.lectures le " +
            "left join fetch le.teacher t left join fetch t.faculty left join fetch le.lesson l left join fetch l.subject " +
            "left join fetch le.audience  left join fetch g.faculty where g.id =:id")
    Optional<Group> findById(@Param("id") Long id);
}
