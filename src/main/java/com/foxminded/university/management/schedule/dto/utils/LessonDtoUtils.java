package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.lesson.LessonDto;
import com.foxminded.university.management.schedule.models.Lesson;
import com.foxminded.university.management.schedule.models.Subject;
import com.foxminded.university.management.schedule.service.impl.SubjectServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class LessonDtoUtils {
    private static SubjectServiceImpl subjectService;

    public LessonDtoUtils(SubjectServiceImpl subjectService) {
        LessonDtoUtils.subjectService = subjectService;
    }

    public static Lesson mapLessonDtoOnLesson(LessonDto lessonDto) {
        Lesson lesson = new Lesson();

        copySubjectFromLessonsDtoToLesson(lessonDto, lesson);

        BeanUtils.copyProperties(lessonDto, lesson);
        return lesson;
    }

    private static void copySubjectFromLessonsDtoToLesson(LessonDto lessonDto, Lesson lesson) {
        Subject subject = new Subject();
        BeanUtils.copyProperties(lessonDto.getSubject(), subject);
        lesson.setSubject(subject);
    }

    public static boolean isSuchSubjectFromLessonDtoExist(LessonDto lessonDto) {
        Subject subject = subjectService.getSubjectById(lessonDto.getSubject().getId());
        String subjectDtoName = lessonDto.getSubject().getName();
        return subject.getName().equals(subjectDtoName);
    }
}
