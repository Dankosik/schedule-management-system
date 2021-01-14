package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;

import java.util.List;
import java.util.Optional;

public interface AudienceService {
    Audience saveAudience(Audience audience);

    Optional<Audience> getAudienceById(Long id);

    List<Audience> getAllAudiences();

    boolean deleteAudienceById(Long id);

    List<Audience> saveAllAudiences(List<Audience> audiences);

    boolean addLectureToAudience(Lecture lecture, Audience audience);

    boolean removeLectureFromAudience(Lecture lecture, Audience audience);
}
