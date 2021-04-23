package com.foxminded.university.management.schedule.service;

import com.foxminded.university.management.schedule.models.Audience;
import com.foxminded.university.management.schedule.models.Lecture;
import com.foxminded.university.management.schedule.repository.AudienceRepository;
import com.foxminded.university.management.schedule.service.exceptions.EntityNotFoundException;
import com.foxminded.university.management.schedule.service.exceptions.UniqueConstraintException;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {AudienceServiceImpl.class})
class AudienceServiceImplTest {
    private final Audience audience = new Audience(1L, 202, 45, null);
    private final List<Audience> audiences = List.of(audience,
            new Audience(2L, 203, 50, null),
            new Audience(3L, 204, 55, null),
            new Audience(4L, 205, 60, null));
    @Autowired
    private AudienceServiceImpl audienceService;
    @MockBean
    private AudienceRepository audienceRepository;

    @Test
    void shouldSaveAudience() {
        when(audienceRepository.saveAndFlush(new Audience(202, 45, null))).thenReturn(audience);
        Audience actual = audienceService.saveAudience(audience);

        assertEquals(audience, actual);

        verify(audienceRepository, times(1)).saveAndFlush(audience);
    }

    @Test
    void shouldReturnAudienceWithIdOne() {
        when(audienceRepository.findById(1L)).thenReturn(Optional.of(audience));
        Audience actual = audienceService.getAudienceById(1L);

        assertEquals(audience, actual);

        verify(audienceRepository, times(2)).findById(1L);
    }

    @Test
    void shouldReturnListOfAudiences() {
        when(audienceRepository.findAll()).thenReturn(audiences);

        assertEquals(audiences, audienceService.getAllAudiences());

        verify(audienceRepository, times(1)).findAll();
    }

    @Test
    void shouldDeleteAudienceWithIdOne() {
        when(audienceRepository.findById(1L)).thenReturn(Optional.of(audience));

        audienceService.deleteAudienceById(1L);

        verify(audienceRepository, times(1)).deleteById(1L);
        verify(audienceRepository, times(3)).findById(1L);
    }

    @Test
    void shouldSaveListOfAudiences() {
        when(audienceRepository.saveAndFlush(new Audience(202, 45, null))).thenReturn(audience);
        when(audienceRepository.saveAndFlush(new Audience(203, 50, null))).thenReturn(audiences.get(1));
        when(audienceRepository.saveAndFlush(new Audience(204, 55, null))).thenReturn(audiences.get(2));
        when(audienceRepository.saveAndFlush(new Audience(205, 60, null))).thenReturn(audiences.get(3));

        List<Audience> actual = audienceService.saveAllAudiences(audiences);

        assertEquals(audiences, actual);

        verify(audienceRepository, times(1)).saveAndFlush(audiences.get(0));
        verify(audienceRepository, times(1)).saveAndFlush(audiences.get(1));
        verify(audienceRepository, times(1)).saveAndFlush(audiences.get(2));
        verify(audienceRepository, times(1)).saveAndFlush(audiences.get(3));
    }

    @Test
    void shouldThrowExceptionIfCreatedAudienceWithInputNumberIsAlreadyExist() {
        Audience expected = new Audience(202, 45, null);

        when(audienceRepository.saveAndFlush(expected)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UniqueConstraintException.class, () -> audienceService.saveAudience(expected));

        verify(audienceRepository, times(1)).saveAndFlush(expected);
    }

    @Test
    void shouldThrowExceptionIfUpdatedAudienceWithInputNumberIsAlreadyExist() {
        Audience expected = new Audience(1L, 202, 45, null);

        when(audienceRepository.saveAndFlush(expected)).thenThrow(DataIntegrityViolationException.class);

        assertThrows(UniqueConstraintException.class, () -> audienceService.saveAudience(expected));

        verify(audienceRepository, times(1)).saveAndFlush(expected);
    }

    @Test
    void shouldThrowExceptionIfAudienceWithInputIdNotFound() {
        when(audienceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> audienceService.getAudienceById(1L));

        verify(audienceRepository, times(1)).findById(1L);
        verify(audienceRepository, never()).save(audience);
    }

    @Test
    void shouldReturnAudienceNumbersForEachAudiences() {
        List<Audience> input = List.of(new Audience(1L, 202, 45, null),
                new Audience(2L, 203, 50, null),
                new Audience(3L, 204, 55, null));

        List<Integer> expected = List.of(202, 203, 204);

        assertEquals(expected, audienceService.getAudienceNumbersWithPossibleNullForAudiences(input));
    }

    @Test
    void shouldReturnAudienceNumbersForEachAudiencesWithIdNull() {
        List<Audience> input = Arrays.asList(null, new Audience(2L, 203, 50, null), null);

        List<Integer> expected = Arrays.asList(null, 203, null);

        assertEquals(expected, audienceService.getAudienceNumbersWithPossibleNullForAudiences(input));
    }

    @Test
    void shouldReturnAudiencesForLectures() {
        List<Audience> expected = List.of(new Audience(1L, 202, 45, null),
                new Audience(2L, 203, 50, null),
                new Audience(3L, 204, 55, null));

        List<Lecture> input = List.of(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(1L, 202, 45, null), null, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(2L, 203, 50, null), null, null, null),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(3L, 204, 55, null), null, null, null));

        assertEquals(expected, audienceService.getAudiencesWithPossibleNullForLectures(input));
    }

    @Test
    void shouldReturnAudiencesForEachLecturesWithIdNull() {
        List<Audience> expected = Arrays.asList(new Audience(1L, 202, 45, null), null,
                new Audience(3L, 204, 55, null));

        List<Lecture> input = Arrays.asList(
                new Lecture(1L, 1, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(1L, 202, 45, null), null, null, null),
                new Lecture(2L, 2, Date.valueOf(LocalDate.of(2021, 1, 1)), null, null, null, null),
                new Lecture(3L, 3, Date.valueOf(LocalDate.of(2021, 1, 1)),
                        new Audience(3L, 204, 55, null), null, null, null));

        assertEquals(expected, audienceService.getAudiencesWithPossibleNullForLectures(input));
    }

    @Test
    void shouldReturnTrueIfAudienceWithIdExist() {
        when(audienceRepository.findById(1L)).thenReturn(Optional.of(new Audience(1L, 1, 1, null)));
        assertTrue(audienceService.isAudienceWithIdExist(1L));
    }

    @Test
    void shouldReturnFalseIfAudienceWithIdNotExist() {
        when(audienceRepository.findById(1L)).thenReturn(Optional.empty());
        assertFalse(audienceService.isAudienceWithIdExist(1L));
    }

    @Test
    void shouldThrowEntityNotFoundExceptionIfAudienceNotExistOnDelete() {
        when(audienceRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> audienceService.deleteAudienceById(1L));

        verify(audienceRepository, times(1)).findById(1L);
        verify(audienceRepository, never()).deleteById(1L);
    }
}
