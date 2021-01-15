package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;

import java.util.List;

public interface AudienceService {
    Audience saveAudience(Audience audience);

    Audience getAudienceById(Long id);

    List<Audience> getAllAudiences();

    void deleteAudienceById(Long id);

    List<Audience> saveAllAudiences(List<Audience> audiences);

    Lecture addLectureToAudience(Lecture lecture, Audience audience);

    Lecture removeLectureFromAudience(Lecture lecture, Audience audience);
}
