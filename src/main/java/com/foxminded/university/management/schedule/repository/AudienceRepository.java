package com.foxminded.university.management.schedule.repository;

import com.foxminded.university.management.schedule.models.Audience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long> {
    @Override
    @Query("select a from Audience a left join fetch a.lectures l left join fetch l.group g left join fetch g.faculty " +
            "left join fetch l.teacher t left join fetch t.faculty left join fetch l.lesson le left join fetch le.subject where a.id =:id")
    Optional<Audience> findById(@Param("id") Long id);
}
