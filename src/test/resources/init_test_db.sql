-- insert test id to tables
INSERT INTO schedule (id) VALUES (1000);
INSERT INTO schedule (id) VALUES (1001);

INSERT INTO university (id) VALUES (1000);
INSERT INTO university (id) VALUES (1001);

INSERT INTO audiences (id) VALUES (1000);
INSERT INTO audiences (id) VALUES (1001);
INSERT INTO audiences (id) VALUES (1002);
INSERT INTO audiences (id) VALUES (1003);
INSERT INTO audiences (id) VALUES (1004);

INSERT INTO faculties (id) VALUES (1000);
INSERT INTO faculties (id) VALUES (1001);

INSERT INTO students (id) VALUES (1000);
INSERT INTO students (id) VALUES (1001);
INSERT INTO students (id) VALUES (1002);
INSERT INTO students (id) VALUES (1003);
INSERT INTO students (id) VALUES (1004);

INSERT INTO subjects (id) VALUES (1000);
INSERT INTO subjects (id) VALUES (1001);
INSERT INTO subjects (id) VALUES (1002);

INSERT INTO departments (id) VALUES (1000);
INSERT INTO departments (id) VALUES (1001);

INSERT INTO lessons (id) VALUES (1000);
INSERT INTO lessons (id) VALUES (1001);
INSERT INTO lessons (id) VALUES (1002);
INSERT INTO lessons (id) VALUES (1003);

INSERT INTO lectures (id) VALUES (1000);
INSERT INTO lectures (id) VALUES (1001);
INSERT INTO lectures (id) VALUES (1002);
INSERT INTO lectures (id) VALUES (1003);

INSERT INTO groups (id) VALUES (1000);
INSERT INTO groups (id) VALUES (1001);

INSERT INTO teachers (id) VALUES (1000);
INSERT INTO teachers (id) VALUES (1001);

-- insert test data to other columns
update schedule set university_id = 1000 where id =1000;
update schedule set university_id = 1000 where id =1001;

update audiences set number = 301, capacity = 50, university_id = 1000 where id = 1000;
update audiences set number = 302, capacity = 75, university_id = 1000 where id = 1001;
update audiences set number = 303, capacity = 100, university_id = 1000 where id = 1002;
update audiences set number = 304, capacity = 30, university_id = 1000 where id = 1003;
update audiences set number = 305, capacity = 55, university_id = 1000 where id = 1004;

update faculties set name = 'FAIT', university_id = 1000 where id = 1000;
update faculties set name = 'FKFN', university_id = 1000 where id = 1001;

update students set first_name = 'Ferdinanda', last_name = 'Casajuana', middle_name = 'Lambarton', course_number = 1, group_id = 1000, faculty_id = 1000, university_id = 1000 where id = 1000;
update students set first_name = 'Lindsey', last_name = 'Syplus', middle_name = 'Slocket', course_number = 1, group_id = 1001, faculty_id = 1000, university_id = 1000 where id = 1001;
update students set first_name = 'Minetta', last_name = 'Funcheon', middle_name = 'Sayle', course_number = 2, group_id = 1000, faculty_id = 1001, university_id = 1000 where id = 1002;
update students set first_name = 'Jessa', last_name = 'Costin', middle_name = 'Heeron', course_number = 2, group_id = 1001, faculty_id = 1001, university_id = 1000 where id = 1003;
update students set first_name = 'Earl', last_name = 'Djekic', middle_name = 'Tremble', course_number = 3, group_id = 1000, faculty_id = 1000, university_id = 1000 where id = 1004;

update lessons set number = 1, start_time = '8:30:00', duration = '1 hour 30 minute', subject_id = 1000 where id = 1000;
update lessons set number = 2, start_time = '10:10:00', duration = '1 hour 30 minute', subject_id = 1001 where id = 1001;
update lessons set number = 3, start_time = '11:50:00', duration = '1 hour 30 minute', subject_id = 1002 where id = 1002;
update lessons set number = 4, start_time = '13:20:00', duration = '1 hour 30 minute', subject_id = 1002 where id = 1003;

update lectures set number = 1, date = '1/1/2021', audience_id = 1000, lesson_id = 1000, teacher_id = 1000, schedule_id = 1000 where id = 1000;
update lectures set number = 2, date = '1/1/2021', audience_id = 1001, lesson_id = 1001, teacher_id = 1001, schedule_id = 1001 where id = 1001;
update lectures set number = 3, date = '1/1/2021', audience_id = 1002, lesson_id = 1002, teacher_id = 1000, schedule_id = 1000 where id = 1002;
update lectures set number = 4, date = '2/1/2021', audience_id = 1003, lesson_id = 1003, teacher_id = 1001, schedule_id = 1001 where id = 1003;

update groups set name = 'AB-91', lecture_id = 1000, department_id = 1000, faculty_id = 1000, university_id = 1000 where id = 1000;
update groups set name = 'BC-01', lecture_id = 1001, department_id = 1001, faculty_id = 1001, university_id = 1000 where id = 1001;

update teachers set first_name = 'Hillel', last_name = 'St. Leger', middle_name = 'Lugard', student_id = 1000, university_id = 1000 where id = 1000;
update teachers set first_name = 'Lynsey', last_name = 'Grzeszczak', middle_name = 'McPhillimey', student_id = 1001, university_id = 1000 where id = 1001;

update subjects set name = 'Math', university_id = 1000 WHERE id = 1000;
update subjects set name = 'Physics', university_id = 1000 WHERE id = 1001;
update subjects set name = 'Programming', university_id = 1000 WHERE id = 1002;

insert into subjects_students (subject_id, student_id) values (1000, 1000);
insert into subjects_students (subject_id, student_id) values (1000, 1001);
insert into subjects_students (subject_id, student_id) values (1000, 1002);
insert into subjects_students (subject_id, student_id) values (1002, 1003);
insert into subjects_students (subject_id, student_id) values (1001, 1004);
insert into subjects_students (subject_id, student_id) values (1002, 1000);
insert into subjects_students (subject_id, student_id) values (1002, 1002);
insert into subjects_students (subject_id, student_id) values (1002, 1004);

insert into subjects_teachers (subject_id, teacher_id) values (1000, 1000);
insert into subjects_teachers (subject_id, teacher_id) values (1001, 1000);
insert into subjects_teachers (subject_id, teacher_id) values (1002, 1001);


update departments set name = 'Department of Automation and System Engineering', faculty_id = 1000, university_id = 1000 WHERE id = 1000;
update departments set name = 'Department of Higher Mathematics', faculty_id = 1001, university_id = 1000  WHERE id = 1001;


