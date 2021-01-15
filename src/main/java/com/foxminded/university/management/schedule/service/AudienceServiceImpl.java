package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.dao.LectureDao;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.exceptions.AudienceServiceException;
import com.foxminded.university.management.schedule.service.exceptions.LectureServiceException;
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
            throw new AudienceServiceException("Audience with number: " + audience.getNumber() + " already exist");
        return audienceDao.save(audience);
    }

    @Override
    public Audience getAudienceById(Long id) {
        if (audienceDao.getById(id).isPresent()) {
            return audienceDao.getById(id).get();
        }
        throw new AudienceServiceException("Audience with id: " + id + " not found");
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
        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (isAudiencePresent && isLecturePresent) {
            lecture.setAudienceId(audience.getId());
            return lectureService.saveLecture(lecture);
        }
        if (!isAudiencePresent)
            throw new LectureServiceException("Cant add lecture to audience cause audience with id: " + audience.getId() + " not exist");
        throw new LectureServiceException("Cant add lecture to audience cause lecture with id: " + lecture.getId() + " not exist");
    }

    @Override
    public Lecture removeLectureFromAudience(Lecture lecture, Audience audience) {
        boolean isAudiencePresent = audienceDao.getById(audience.getId()).isPresent();
        boolean isLecturePresent = lectureDao.getById(lecture.getId()).isPresent();
        if (isAudiencePresent && isLecturePresent) {
            lecture.setAudienceId(null);
            return lectureService.saveLecture(lecture);
        }
        if (!isAudiencePresent)
            throw new LectureServiceException("Cant remove lecture from audience cause audience with id: " + audience.getId() + " not exist");
        throw new LectureServiceException("Cant remove lecture from audience cause lecture with id: " + lecture.getId() + " not exist");
    }
}
