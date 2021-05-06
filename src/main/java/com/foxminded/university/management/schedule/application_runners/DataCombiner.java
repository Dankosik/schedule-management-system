package com.foxminded.university.management.schedule.application_runners;

import com.foxminded.university.management.schedule.models.*;
import com.foxminded.university.management.schedule.service.*;
import com.foxminded.university.management.schedule.service.data.generation.DataGenerator;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("!test")
@Component
public class DataCombiner implements ApplicationRunner {
    private final AudienceService audienceService;
    private final FacultyService facultyService;
    private final GroupService groupService;
    private final LessonService lessonService;
    private final LectureService lectureService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final DataGenerator<Audience> audienceDataGenerator;
    private final DataGenerator<Faculty> facultyDataGenerator;
    private final DataGenerator<Group> groupDataGenerator;
    private final DataGenerator<Subject> subjectDataGenerator;
    private final DataGenerator<Student> studentDataGenerator;
    private final DataGenerator<Teacher> teacherDataGenerator;
    private final DataGenerator<Lesson> lessonDataGenerator;
    private final DataGenerator<Lecture> lectureDataGenerator;

    public DataCombiner(AudienceService audienceService, FacultyService facultyService,
                        GroupService groupService, LessonService lessonService, LectureService lectureService,
                        StudentService studentService, SubjectService subjectService, TeacherService teacherService,
                        DataGenerator<Audience> audienceDataGenerator, DataGenerator<Faculty> facultyDataGenerator,
                        DataGenerator<Group> groupDataGenerator, DataGenerator<Subject> subjectDataGenerator,
                        DataGenerator<Student> studentDataGenerator, DataGenerator<Teacher> teacherDataGenerator,
                        DataGenerator<Lesson> lessonDataGenerator, DataGenerator<Lecture> lectureDataGenerator) {

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
