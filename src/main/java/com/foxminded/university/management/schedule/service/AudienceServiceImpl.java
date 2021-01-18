package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.exceptions.AudienceServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AudienceServiceImpl implements AudienceService {
    @Autowired
    private AudienceDao audienceDao;
    @Autowired
    private LectureDao lectureDao;
    @Autowired
    private LectureServiceImpl lectureService;

    @Override
    public Audience saveAudience(Audience audience) {
        Optional<Audience> audienceWithSameNumber = audienceDao.getAll()
                .stream()
                .filter(a -> a.getNumber() == audience.getNumber())
                .findAny();
        if (audienceWithSameNumber.isPresent())
            throw new AudienceServiceException("Audience with number: " + audience.getNumber() + " is already exists");
        return audienceDao.save(audience);
    }

    @Override
    public Audience getAudienceById(Long id) {
        if (audienceDao.getById(id).isPresent()) {
            return audienceDao.getById(id).get();
        }
        throw new AudienceServiceException("Audience with id: " + id + " is not found");
    }

    @Override
    public List<Audience> getAllAudiences() {
        return audienceDao.getAll();
    }

    @Override
    public void deleteAudienceById(Long id) {
        audienceDao.deleteById(getAudienceById(id).getId());
    }

    @Override
    public List<Audience> saveAllAudiences(List<Audience> audiences) {
        List<Audience> result = new ArrayList<>();
        audiences.forEach(audience -> result.add(saveAudience(audience)));
        return result;
    }

    @Override
    public Lecture addLectureToAudience(Lecture lecture, Audience audience) {
        boolean isAudiencePresent = audienceDao.getById(audience.getId()).isPresent();
        if (!isAudiencePresent)
            throw new AudienceServiceException("Impossible to add lecture to audience. Audience with id: " + audience.getId() + " is not exists");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new AudienceServiceException("Impossible to add lecture to audience. Lecture with id: " + lecture.getId() + " is not exists");

        if (lecture.getAudienceId() != null && lecture.getAudienceId().equals(audience.getId()))
            throw new AudienceServiceException("Lecture with id: " + lecture.getId() + " is already added to audience with id: " + audience.getId());

        lecture.setAudienceId(audience.getId());
        return lectureService.saveLecture(lecture);
    }

    @Override
    public Lecture removeLectureFromAudience(Lecture lecture, Audience audience) {
        boolean isAudiencePresent = audienceDao.getById(audience.getId()).isPresent();
        if (!isAudiencePresent)
            throw new AudienceServiceException("Impossible to remove lecture from audience. Audience with id: " + audience.getId() + " is not exists");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new AudienceServiceException("Impossible to remove lecture from audience. Lecture with id: " + lecture.getId() + " is not exists");

        if (lecture.getAudienceId() == null)
            throw new AudienceServiceException("Lecture with id: " + lecture.getId() + " is already removed from audience with id: " + audience.getId());

        lecture.setAudienceId(null);
        return lectureService.saveLecture(lecture);
    }
}
