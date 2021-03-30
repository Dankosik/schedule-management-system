package com.foxminded.university.management.schedule.models.formatters;

import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Component
public class AudienceFormatter implements Formatter<Audience> {
    private final AudienceServiceImpl audienceService;

    public AudienceFormatter(AudienceServiceImpl audienceService) {
        this.audienceService = audienceService;
    }

    @Override
    public Audience parse(String s, Locale locale) throws ParseException {
        return audienceService.getAudienceById(Long.valueOf(s));
    }

    @Override
    public String print(Audience audience, Locale locale) {
        return audience.toString();
    }
}
