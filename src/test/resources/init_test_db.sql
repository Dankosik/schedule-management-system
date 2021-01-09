INSERT INTO university (id)
VALUES (1000);
INSERT INTO university (id)
VALUES (1001);

INSERT INTO audiences (id, number, capacity, university_id)
VALUES (1000, 301, 50, 1000);
INSERT INTO audiences (id, number, capacity, university_id)
VALUES (1001, 302, 75, 1000);
INSERT INTO audiences (id, number, capacity, university_id)
VALUES (1002, 303, 100, 1000);
INSERT INTO audiences (id, number, capacity, university_id)
VALUES (1003, 304, 30, 1000);
INSERT INTO audiences (id, number, capacity, university_id)
VALUES (1004, 305, 55, 1000);

INSERT INTO faculties (id, name, university_id)
VALUES (1000, 'FAIT', 1000);
INSERT INTO faculties (id, name, university_id)
VALUES (1001, 'FKFN', 1000);

INSERT INTO groups (id, name, faculty_id, university_id)
VALUES (1000, 'AB-91', 1000, 1000);
INSERT INTO groups (id, name, faculty_id, university_id)
VALUES (1001, 'BC-01', 1001, 1000);

INSERT INTO students (id, first_name, last_name, middle_name, course_number, group_id, university_id)
VALUES (1000, 'Ferdinanda', 'Casajuana', 'Lambarton', 1, 1000, 1000);
INSERT INTO students (id, first_name, last_name, middle_name, course_number, group_id, university_id)
VALUES (1001, 'Lindsey', 'Syplus', 'Slocket', 1, 1001, 1000);
INSERT INTO students (id, first_name, last_name, middle_name, course_number, group_id, university_id)
VALUES (1002, 'Minetta', 'Funcheon', 'Sayle', 2, 1000, 1000);
INSERT INTO students (id, first_name, last_name, middle_name, course_number, group_id, university_id)
VALUES (1003, 'Jessa', 'Costin', 'Heeron', 2, 1001, 1000);
INSERT INTO students (id, first_name, last_name, middle_name, course_number, group_id, university_id)
VALUES (1004, 'Earl', 'Djekic', 'Tremble', 3, 1000, 1000);

INSERT INTO subjects (id, name, university_id)
VALUES (1000, 'Math', 1000);
INSERT INTO subjects (id, name, university_id)
VALUES (1001, 'Physics', 1000);
INSERT INTO subjects (id, name, university_id)
VALUES (1002, 'Programming', 1000);

INSERT INTO lessons (id, number, start_time, duration, subject_id)
VALUES (1000, 1, '8:30:00', '1 hour 30 minute', 1000);
INSERT INTO lessons (id, number, start_time, duration, subject_id)
VALUES (1001, 2, '10:10:00', '1 hour 30 minute', 1001);
INSERT INTO lessons (id, number, start_time, duration, subject_id)
VALUES (1002, 3, '11:50:00', '1 hour 30 minute', 1002);
INSERT INTO lessons (id, number, start_time, duration, subject_id)
VALUES (1003, 4, '13:20:00', '1 hour 30 minute', 1002);

INSERT INTO teachers (id, first_name, last_name, middle_name, faculty_id, university_id)
VALUES (1000, 'Hillel', 'St. Leger', 'Lugard', 1000, 1000);
INSERT INTO teachers (id, first_name, last_name, middle_name, faculty_id, university_id)
VALUES (1001, 'Lynsey', 'Grzeszczak', 'McPhillimey', 1001, 1000);


INSERT INTO lectures (id, number, date, audience_id, lesson_id, teacher_id)
VALUES (1000, 1, '1/1/2021', 1000, 1000, 1000);
INSERT INTO lectures (id, number, date, audience_id, lesson_id, teacher_id)
VALUES (1001, 2, '1/1/2021', 1001, 1001, 1001);
INSERT INTO lectures (id, number, date, audience_id, lesson_id, teacher_id)
VALUES (1002, 3, '1/1/2021', 1002, 1002, 1000);
INSERT INTO lectures (id, number, date, audience_id, lesson_id, teacher_id)
VALUES (1003, 4, '2/1/2021', 1003, 1003, 1001);

insert into subjects_students (subject_id, student_id)
values (1000, 1000);
insert into subjects_students (subject_id, student_id)
values (1000, 1001);
insert into subjects_students (subject_id, student_id)
values (1000, 1002);
insert into subjects_students (subject_id, student_id)
values (1002, 1003);
insert into subjects_students (subject_id, student_id)
values (1001, 1004);
insert into subjects_students (subject_id, student_id)
values (1002, 1000);
insert into subjects_students (subject_id, student_id)
values (1002, 1002);
insert into subjects_students (subject_id, student_id)
values (1002, 1004);

insert into subjects_teachers (subject_id, teacher_id)
values (1000, 1000);
insert into subjects_teachers (subject_id, teacher_id)
values (1001, 1000);
insert into subjects_teachers (subject_id, teacher_id)
values (1002, 1001);