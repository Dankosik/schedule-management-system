-- creating tables
create table schedule
(

);

create table university
(

);

create table audiences
(

);

create table faculties
(

);

create table departments
(

);

create table lectures
(

);

create table groups
(

);

create table students
(

);

create table lessons
(

);

create table subjects
(

);

create table teachers
(

);

create table subjects_teachers
(
     subject_id BIGINT REFERENCES subjects (id) ON DELETE CASCADE,
     teacher_id BIGINT REFERENCES teachers (id) ON DELETE CASCADE,
     PRIMARY KEY (subject_id, teacher_id)
);

create table subjects_students
(
    subject_id BIGINT REFERENCES subjects (id) ON DELETE CASCADE,
    student_id BIGINT REFERENCES teachers (id) ON DELETE CASCADE,
    PRIMARY KEY (subject_id, student_id)
);

-- adding id to tables
ALTER TABLE schedule
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE university
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE audiences
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE faculties
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE departments
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE lectures
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE groups
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE students
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE lessons
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE subjects
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

ALTER TABLE teachers
    ADD COLUMN id BIGSERIAL NOT NULL PRIMARY KEY;

-- adding other columns to tables
ALTER TABLE university
    ADD COLUMN schedule_id BIGINT  REFERENCES schedule (id) ON DELETE CASCADE;

ALTER TABLE audiences
    ADD COLUMN number        INT   UNIQUE,
    ADD COLUMN capacity      INT    ,
    ADD COLUMN university_id BIGINT  REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE faculties
    ADD COLUMN name          varchar(256) UNIQUE,
    ADD COLUMN university_id BIGINT        REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE departments
    ADD COLUMN name          varchar(256) UNIQUE,
    ADD COLUMN faculty_id    BIGINT        REFERENCES faculties (id) ON DELETE CASCADE,
    ADD COLUMN university_id BIGINT        REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE lectures
    ADD COLUMN number      INT   ,
    ADD COLUMN date        DATE   ,
    ADD COLUMN audience_id BIGINT  REFERENCES audiences (id) ON DELETE SET NULL,
    ADD COLUMN lesson_id   BIGINT  REFERENCES lessons (id) ON DELETE SET NULL,
    ADD COLUMN teacher_id  BIGINT  REFERENCES teachers (id) ON DELETE SET NULL,
    ADD COLUMN schedule_id BIGINT  REFERENCES schedule (id) ON DELETE CASCADE;

ALTER TABLE groups
    ADD COLUMN name          varchar(256) UNIQUE,
    ADD COLUMN lecture_id    BIGINT        REFERENCES lectures (id) ON DELETE SET NULL,
    ADD COLUMN department_id BIGINT        REFERENCES departments (id) ON DELETE SET NULL,
    ADD COLUMN faculty_id    BIGINT        REFERENCES faculties (id) ON DELETE CASCADE,
    ADD COLUMN university_id BIGINT        REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE students
    ADD COLUMN first_name    varchar(50),
    ADD COLUMN last_name     varchar(50),
    ADD COLUMN middle_name   varchar(50),
    ADD COLUMN course_number INT         ,
    ADD COLUMN group_id      BIGINT      REFERENCES groups (id) ON DELETE SET NULL,
    ADD COLUMN faculty_id    BIGINT      REFERENCES faculties (id) ON DELETE SET NULL,
    ADD COLUMN university_id BIGINT       REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE lessons
    ADD COLUMN number     INT      ,
    ADD COLUMN start_time TIME     ,
    ADD COLUMN duration   INTERVAL,
    ADD COLUMN subject_id BIGINT    REFERENCES subjects (id) ON DELETE SET NULL;

ALTER TABLE subjects
    ADD COLUMN name          varchar(256) UNIQUE,
    ADD COLUMN student_id    BIGINT        REFERENCES students (id) ON DELETE SET NULL,
    ADD COLUMN teacher_id    BIGINT        REFERENCES teachers (id) ON DELETE SET NULL,
    ADD COLUMN university_id BIGINT        REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE teachers
    ADD COLUMN first_name    varchar(50) ,
    ADD COLUMN last_name     varchar(50) ,
    ADD COLUMN middle_name   varchar(50) ,
    ADD COLUMN student_id    BIGINT       REFERENCES students (id) ON DELETE SET NULL,
    ADD COLUMN university_id BIGINT       REFERENCES university (id) ON DELETE CASCADE;

-- insert test id to tables
INSERT INTO schedule (id) VALUES (1);
INSERT INTO schedule (id) VALUES (2);
INSERT INTO schedule (id) VALUES (3);
INSERT INTO schedule (id) VALUES (4);
INSERT INTO schedule (id) VALUES (5);

INSERT INTO university (id) VALUES (1);

INSERT INTO audiences (id) VALUES (1);
INSERT INTO audiences (id) VALUES (2);
INSERT INTO audiences (id) VALUES (3);
INSERT INTO audiences (id) VALUES (4);
INSERT INTO audiences (id) VALUES (5);

INSERT INTO faculties (id) VALUES (1);
INSERT INTO faculties (id) VALUES (2);

INSERT INTO students (id) VALUES (1);
INSERT INTO students (id) VALUES (2);
INSERT INTO students (id) VALUES (3);
INSERT INTO students (id) VALUES (4);
INSERT INTO students (id) VALUES (5);

INSERT INTO subjects (id) VALUES (1);
INSERT INTO subjects (id) VALUES (2);
INSERT INTO subjects (id) VALUES (3);

INSERT INTO departments (id) VALUES (1);
INSERT INTO departments (id) VALUES (2);

INSERT INTO lessons (id) VALUES (1);
INSERT INTO lessons (id) VALUES (2);
INSERT INTO lessons (id) VALUES (3);
INSERT INTO lessons (id) VALUES (4);

INSERT INTO lectures (id) VALUES (1);
INSERT INTO lectures (id) VALUES (2);
INSERT INTO lectures (id) VALUES (3);
INSERT INTO lectures (id) VALUES (4);

INSERT INTO groups (id) VALUES (1);
INSERT INTO groups (id) VALUES (2);

INSERT INTO teachers (id) VALUES (1);
INSERT INTO teachers (id) VALUES (2);

-- insert test data to other columns
update university set schedule_id = 1 where id = 1;
update university set schedule_id = 2 where id = 1;

update audiences set number = 301, capacity = 50, university_id = 1 where id = 1;
update audiences set number = 302, capacity = 75, university_id = 1 where id = 2;
update audiences set number = 303, capacity = 100, university_id = 1 where id = 3;
update audiences set number = 304, capacity = 30, university_id = 1 where id = 4;
update audiences set number = 305, capacity = 55, university_id = 1 where id = 5;

update faculties set name = 'FAIT', university_id = 1 where id = 1;
update faculties set name = 'FKFN', university_id = 1 where id = 2;

update students set first_name = 'Ferdinanda', last_name = 'Casajuana', middle_name = 'Lambarton', course_number = 1, group_id = 1, faculty_id = 1, university_id = 1 where id = 1;
update students set first_name = 'Lindsey', last_name = 'Syplus', middle_name = 'Slocket', course_number = 1, group_id = 2, faculty_id = 1, university_id = 1 where id = 2;
update students set first_name = 'Minetta', last_name = 'Funcheon', middle_name = 'Sayle', course_number = 2, group_id = 1, faculty_id = 2, university_id = 1 where id = 3;
update students set first_name = 'Jessa', last_name = 'Costin', middle_name = 'Heeron', course_number = 2, group_id = 2, faculty_id = 2, university_id = 1 where id = 4;
update students set first_name = 'Earl', last_name = 'Djekic', middle_name = 'Tremble', course_number = 3, group_id = 1, faculty_id = 1, university_id = 1 where id = 5;

update lessons set number = 1, start_time = '8:30:00', duration = '1 hour 30 minute', subject_id = 1 where id = 1;
update lessons set number = 2, start_time = '10:10:00', duration = '1 hour 30 minute', subject_id = 2 where id = 2;
update lessons set number = 3, start_time = '11:50:00', duration = '1 hour 30 minute', subject_id = 3 where id = 3;
update lessons set number = 4, start_time = '13:20:00', duration = '1 hour 30 minute', subject_id = 3 where id = 4;

update lectures set number = 1, date = '1/1/2021', audience_id = 1, lesson_id = 1, teacher_id = 1, schedule_id = 1 where id = 1;
update lectures set number = 2, date = '1/1/2021', audience_id = 2, lesson_id = 2, teacher_id = 2, schedule_id = 2 where id = 2;
update lectures set number = 3, date = '1/1/2021', audience_id = 3, lesson_id = 3, teacher_id = 1, schedule_id = 1 where id = 3;
update lectures set number = 4, date = '2/1/2021', audience_id = 4, lesson_id = 4, teacher_id = 2, schedule_id = 2 where id = 4;

update groups set name = 'AB-91', lecture_id = 1, department_id = 1, faculty_id = 1, university_id = 1 where id = 1;
update groups set name = 'BC-01', lecture_id = 2, department_id = 2, faculty_id = 2, university_id = 1 where id = 2;

update teachers set first_name = 'Hillel', last_name = 'St. Leger', middle_name = 'Lugard', student_id = 1, university_id = 1 where id = 1;
update teachers set first_name = 'Lynsey', last_name = 'Grzeszczak', middle_name = 'McPhillimey', student_id = 2, university_id = 1 where id = 2;

update subjects set name = 'Math', university_id = 1 WHERE id = 1;
update subjects set name = 'Physics', university_id = 1 WHERE id = 2;
update subjects set name = 'Programming', university_id = 1 WHERE id = 3;

insert into subjects_students (subject_id, student_id) values (1, 1);
insert into subjects_students (subject_id, student_id) values (1, 2);
insert into subjects_students (subject_id, student_id) values (1, 3);
insert into subjects_students (subject_id, student_id) values (2, 4);
insert into subjects_students (subject_id, student_id) values (2, 5);
insert into subjects_students (subject_id, student_id) values (3, 1);
insert into subjects_students (subject_id, student_id) values (3, 3);
insert into subjects_students (subject_id, student_id) values (3, 5);

insert into subjects_teachers (subject_id, teacher_id) values (1, 1);
insert into subjects_teachers (subject_id, teacher_id) values (2, 1);
insert into subjects_teachers (subject_id, teacher_id) values (3, 2);


update departments set name = 'Department of Automation and System Engineering', faculty_id = 1, university_id = 1 WHERE id = 1;
update departments set name = 'Department of Higher Mathematics', faculty_id = 2, university_id = 1  WHERE id = 2;


