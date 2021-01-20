package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.AudienceService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AudienceServiceImpl implements AudienceService {
    private final AudienceDao audienceDao;
    private final LectureDao lectureDao;
    private final LectureServiceImpl lectureService;

    public AudienceServiceImpl(AudienceDao audienceDao, LectureDao lectureDao, LectureServiceImpl lectureService) {
        this.audienceDao = audienceDao;
        this.lectureDao = lectureDao;
        this.lectureService = lectureService;
    }

    @Override
    public Audience saveAudience(Audience audience) {
        try {
            return audienceDao.save(audience);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("Audience with number: " + audience.getNumber() + " is already exists");
        }
    }

    @Override
    public Audience getAudienceById(Long id) {
        if (audienceDao.getById(id).isPresent()) {
            return audienceDao.getById(id).get();
        }
        throw new ServiceException("Audience with id: " + id + " is not found");
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
            throw new ServiceException("Impossible to add lecture to audience. Audience with id: " + audience.getId() + " is not exists");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new ServiceException("Impossible to add lecture to audience. Lecture with id: " + lecture.getId() + " is not exists");

        if (lecture.getAudienceId() != null && lecture.getAudienceId().equals(audience.getId()))
            throw new ServiceException("Lecture with id: " + lecture.getId() + " is already added to audience with id: " + audience.getId());

        lecture.setAudienceId(audience.getId());
        return lectureService.saveLecture(lecture);
    }

    @Override
    public Lecture removeLectureFromAudience(Lecture lecture, Audience audience) {
        boolean isAudiencePresent = audienceDao.getById(audience.getId()).isPresent();
        if (!isAudiencePresent)
            throw new ServiceException("Impossible to remove lecture from audience. Audience with id: " + audience.getId() + " is not exists");

        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (!isLecturePresent)
            throw new ServiceException("Impossible to remove lecture from audience. Lecture with id: " + lecture.getId() + " is not exists");

        if (lecture.getAudienceId() == null)
            throw new ServiceException("Lecture with id: " + lecture.getId() + " is already removed from audience with id: " + audience.getId());

        lecture.setAudienceId(null);
        return lectureService.saveLecture(lecture);
    }
}
