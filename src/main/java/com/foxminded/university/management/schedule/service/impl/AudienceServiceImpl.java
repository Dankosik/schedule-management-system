package com.foxminded.university.management.schedule.service.impl;

import com.foxminded.university.management.schedule.dao.AudienceDao;
import com.foxminded.university.management.schedule.exceptions.ServiceException;
import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.service.AudienceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AudienceServiceImpl implements AudienceService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AudienceServiceImpl.class);
    private final AudienceDao audienceDao;

    public AudienceServiceImpl(AudienceDao audienceDao) {
        this.audienceDao = audienceDao;
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
        boolean isAudiencePresent = audienceDao.getById(id).isPresent();
        LOGGER.debug("Audience is present: {}", isAudiencePresent);
        if (isAudiencePresent) {
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
    public List<Integer> getAudienceNumbersWithPossibleNullForAudiences(List<Audience> audiences) {
        LOGGER.debug("Getting audience numbers for audiences {}", audiences);
        List<Integer> result = new ArrayList<>();
        for (Audience audience : audiences) {
            if (audience == null) {
                result.add(null);
            } else {
                result.add(audience.getNumber());
            }
        }
        LOGGER.info("Audience numbers for audiences {} received successful", audiences);
        return result;
    }

    @Override
    public List<Audience> getAudiencesWithPossibleNullForLectures(List<Lecture> lectures) {
        LOGGER.debug("Getting audiences for lectures {}", lectures);
        List<Audience> result = new ArrayList<>();
        for (Lecture lecture : lectures) {
            if (lecture.getAudience() == null) {
                result.add(null);
            } else {
                result.add(lecture.getAudience());
            }
        }
        LOGGER.info("Audiences for lectures {} received successful", lectures);
        return result;
    }
}
