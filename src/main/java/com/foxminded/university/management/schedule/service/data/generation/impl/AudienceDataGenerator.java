package com.foxminded.university.management.schedule.service.data.generation.impl;

import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import com.foxminded.university.management.schedule.service.data.generation.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AudienceDataGenerator implements DataGenerator<Audience> {
    @Override
    public List<Audience> generateData() {
        List<Audience> result = new ArrayList<>();
        for (int i = 0; i < RandomUtils.random(1, 10); i++) {
            Audience audience = new Audience();
            audience.setNumber(generateAudienceNumber());
            audience.setUniversityId(1L);
            audience.setCapacity(generateAudienceCapacity());
            result.add(audience);
        }
        return result;
    }

    private int generateAudienceNumber() {
        int min = 100;
        int max = 999;
        return RandomUtils.random(min, max);
    }

    private int generateAudienceCapacity() {
        int min = 10;
        int max = 100;
        return RandomUtils.random(min, max);
    }
}
