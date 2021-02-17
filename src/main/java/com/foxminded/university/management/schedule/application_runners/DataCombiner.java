package com.foxminded.university.management.schedule.application_runners;

import com.foxminded.university.management.schedule.service.data.generation.impl.*;
import com.foxminded.university.management.schedule.service.impl.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class DataCombiner implements ApplicationRunner {
    private final AudienceServiceImpl audienceService;
    private final FacultyServiceImpl facultyService;
    private final GroupServiceImpl groupService;
    private final LessonServiceImpl lessonService;
    private final LectureServiceImpl lectureService;
    private final StudentServiceImpl studentService;
    private final SubjectServiceImpl subjectService;
    private final TeacherServiceImpl teacherService;
    private final AudienceDataGenerator audienceDataGenerator;
    private final FacultyDataGenerator facultyDataGenerator;
    private final GroupDataGenerator groupDataGenerator;
    private final SubjectDataGenerator subjectDataGenerator;
    private final StudentDataGenerator studentDataGenerator;
    private final TeacherDataGenerator teacherDataGenerator;
    private final LessonDataGenerator lessonDataGenerator;
    private final LectureDataGenerator lectureDataGenerator;

    public DataCombiner(AudienceServiceImpl audienceService, FacultyServiceImpl facultyService,
                        GroupServiceImpl groupService, LessonServiceImpl lessonService, LectureServiceImpl lectureService,
                        StudentServiceImpl studentService, SubjectServiceImpl subjectService, TeacherServiceImpl teacherService,
                        AudienceDataGenerator audienceDataGenerator, FacultyDataGenerator facultyDataGenerator,
                        GroupDataGenerator groupDataGenerator, SubjectDataGenerator subjectDataGenerator,
                        StudentDataGenerator studentDataGenerator, TeacherDataGenerator teacherDataGenerator,
                        LessonDataGenerator lessonDataGenerator, LectureDataGenerator lectureDataGenerator) {

        this.audienceService = audienceService;
        this.facultyService = facultyService;
        this.groupService = groupService;
        this.lessonService = lessonService;
        this.lectureService = lectureService;
        this.studentService = studentService;
        this.subjectService = subjectService;
        this.teacherService = teacherService;
        this.audienceDataGenerator = audienceDataGenerator;
        this.facultyDataGenerator = facultyDataGenerator;
        this.groupDataGenerator = groupDataGenerator;
        this.subjectDataGenerator = subjectDataGenerator;
        this.studentDataGenerator = studentDataGenerator;
        this.teacherDataGenerator = teacherDataGenerator;
        this.lessonDataGenerator = lessonDataGenerator;
        this.lectureDataGenerator = lectureDataGenerator;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (audienceService.getAllAudiences().size() == 0)
            audienceService.saveAllAudiences(audienceDataGenerator.generateData());

        if (facultyService.getAllFaculties().size() == 0)
            facultyService.saveAllFaculties(facultyDataGenerator.generateData());

        if (groupService.getAllGroups().size() == 0)
            groupService.saveAllGroups(groupDataGenerator.generateData());

        if (subjectService.getAllSubjects().size() == 0)
            subjectService.saveAllSubjects(subjectDataGenerator.generateData());

        if (studentService.getAllStudents().size() == 0)
            studentService.saveAllStudents(studentDataGenerator.generateData());

        if (teacherService.getAllTeachers().size() == 0)
            teacherService.saveAllTeachers(teacherDataGenerator.generateData());

        if (lectureService.getAllLectures().size() == 0)
            lessonService.saveAllLessons(lessonDataGenerator.generateData());

        if (lectureService.getAllLectures().size() == 0)
            lectureService.saveAllLectures(lectureDataGenerator.generateData());
    }
}
