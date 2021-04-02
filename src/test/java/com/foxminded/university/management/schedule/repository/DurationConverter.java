package com.foxminded.university.management.schedule.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Converter(autoApply = true)
public class DurationConverter implements AttributeConverter<Duration, Long> {
     
    Logger log = LoggerFactory.getLogger(DurationConverter.class.getSimpleName());
 
    @Override
    public Long convertToDatabaseColumn(Duration attribute) {
        log.info("Convert to Long");
        return attribute.toNanos();
    }
 
    @Override
    public Duration convertToEntityAttribute(Long duration) {
        log.info("Convert to Duration");
        return Duration.of(duration, ChronoUnit.NANOS);
    }
}
