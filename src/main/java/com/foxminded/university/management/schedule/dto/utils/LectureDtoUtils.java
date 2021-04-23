package com.foxminded.university.management.schedule.dto.utils;

import com.foxminded.university.management.schedule.dto.faculty.FacultyUpdateDto;
import com.foxminded.university.management.schedule.dto.lecture.LectureDto;
import com.foxminded.university.management.schedule.dto.subject.SubjectUpdateDto;
import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.impl.AudienceServiceImpl;
import com.foxminded.university.management.schedule.service.impl.GroupServiceImpl;
import com.foxminded.university.management.schedule.service.impl.LessonServiceImpl;
import com.foxminded.university.management.schedule.service.impl.TeacherServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.sql.Time;
import java.time.Duration;

@Component
public class LectureDtoUtils {
    private static LessonServiceImpl lessonService;
    private static GroupServiceImpl groupService;
    private static TeacherServiceImpl teacherService;
    private static AudienceServiceImpl audienceService;

    public LectureDtoUtils(LessonServiceImpl lessonService, GroupServiceImpl groupService, TeacherServiceImpl teacherService,
                           AudienceServiceImpl audienceService) {
        LectureDtoUtils.lessonService = lessonService;
        LectureDtoUtils.groupService = groupService;
        LectureDtoUtils.teacherService = teacherService;
        LectureDtoUtils.audienceService = audienceService;
    }

    public static boolean isSuchLessonFromLectureDtoExist(LectureDto lectureDto) {
        Lesson lesson = lessonService.getLessonById(lectureDto.getLesson().getId());
        Integer lessonDtoNumber = lectureDto.getLesson().getNumber();
        Time lessonDtoStarTime = lectureDto.getLesson().getStartTime();
        Duration lessonDtoDuration = lectureDto.getLesson().getDuration();
        SubjectUpdateDto lessonDtoSubject = lectureDto.getLesson().getSubject();
        Subject mappedLessonDtoSubject = new Subject();
        BeanUtils.copyProperties(lessonDtoSubject, mappedLessonDtoSubject);
        return lesson.getNumber().equals(lessonDtoNumber) && lesson.getDuration().equals(lessonDtoDuration) &&
                lesson.getStartTime().equals(lessonDtoStarTime) && lesson.getSubject().getName().equals(mappedLessonDtoSubject.getName()) &&
                lesson.getSubject().getId().equals(mappedLessonDtoSubject.getId());
    }

    public static boolean isSuchGroupFromLectureDtoExist(LectureDto lectureDto) {
        Group group = groupService.getGroupById(lectureDto.getGroup().getId());
        String groupDtoName = lectureDto.getGroup().getName();
        FacultyUpdateDto groupDtoFaculty = lectureDto.getGroup().getFaculty();
        Faculty mappedGroupDtoFaculty = new Faculty();
        BeanUtils.copyProperties(groupDtoFaculty, mappedGroupDtoFaculty);
        return isSuchGroupExist(group, groupDtoName, mappedGroupDtoFaculty);
    }

    private static boolean isSuchGroupExist(Group group, String groupDtoName, Faculty mappedGroupDtoFaculty) {
        return group.getName().equals(groupDtoName) && group.getFaculty().getName().equals(mappedGroupDtoFaculty.getName()) &&
                group.getFaculty().getId().equals(mappedGroupDtoFaculty.getId());
    }

    public static boolean isSuchTeacherFromLectureDtoExist(LectureDto lectureDto) {
        Teacher teacher = teacherService.getTeacherById(lectureDto.getTeacher().getId());
        String teacherDtoFirstName = lectureDto.getTeacher().getFirstName();
        String teacherDtoMiddleName = lectureDto.getTeacher().getMiddleName();
        String teacherDtoLastName = lectureDto.getTeacher().getLastName();
        FacultyUpdateDto teacherDtoFaculty = lectureDto.getTeacher().getFaculty();
        Faculty mappedTeacherDtoFaculty = new Faculty();
        BeanUtils.copyProperties(teacherDtoFaculty, mappedTeacherDtoFaculty);
        return teacher.getFirstName().equals(teacherDtoFirstName) && teacher.getMiddleName().equals(teacherDtoMiddleName) &&
                teacher.getLastName().equals(teacherDtoLastName) && teacher.getFaculty().getName().equals(mappedTeacherDtoFaculty.getName()) &&
                teacher.getFaculty().getId().equals(mappedTeacherDtoFaculty.getId());
    }

    public static boolean isSuchAudienceFromLectureDtoExist(LectureDto lectureDto) {
        Audience audience = audienceService.getAudienceById(lectureDto.getAudience().getId());
        Integer audienceDtoNumber = lectureDto.getAudience().getNumber();
        Integer audienceDtoCapacity = lectureDto.getAudience().getCapacity();
        return audience.getNumber().equals(audienceDtoNumber) && audience.getCapacity().equals(audienceDtoCapacity);
    }

    public static Lecture mapLectureDtoOnLecture(LectureDto lectureDto) {
        Lecture lecture = new Lecture();

        copyAudienceFromLectureDtoToLecture(lectureDto, lecture);

        copyGroupFromLectureDtoToLecture(lectureDto, lecture);

        copyLessonFromLectureDtoToLecture(lectureDto, lecture);

        copyTeacherFromLectureDtoToLecture(lectureDto, lecture);

        BeanUtils.copyProperties(lectureDto, lecture);
        return lecture;
    }

    private static void copyTeacherFromLectureDtoToLecture(LectureDto lectureDto, Lecture lecture) {
        Teacher teacher = new Teacher();
        Faculty teacherFaculty = new Faculty();
        BeanUtils.copyProperties(lectureDto.getTeacher().getFaculty(), teacherFaculty);
        teacher.setFaculty(teacherFaculty);
        BeanUtils.copyProperties(lectureDto.getTeacher(), teacher);
        lecture.setTeacher(teacher);
    }

    private static void copyLessonFromLectureDtoToLecture(LectureDto lectureDto, Lecture lecture) {
        Lesson lesson = new Lesson();
        Subject lessonSubject = new Subject();
        BeanUtils.copyProperties(lectureDto.getLesson().getSubject(), lessonSubject);
        lesson.setSubject(lessonSubject);
        BeanUtils.copyProperties(lectureDto.getLesson(), lesson);
        lecture.setLesson(lesson);
    }

    private static void copyAudienceFromLectureDtoToLecture(LectureDto lectureDto, Lecture lecture) {
        Audience audience = new Audience();
        BeanUtils.copyProperties(lectureDto.getAudience(), audience);
        lecture.setAudience(audience);
    }

    private static void copyGroupFromLectureDtoToLecture(LectureDto lectureDto, Lecture lecture) {
        Group group = new Group();
        Faculty groupFaculty = new Faculty();
        BeanUtils.copyProperties(lectureDto.getGroup().getFaculty(), groupFaculty);
        group.setFaculty(groupFaculty);
        BeanUtils.copyProperties(lectureDto.getGroup(), group);
        lecture.setGroup(group);
    }
}
