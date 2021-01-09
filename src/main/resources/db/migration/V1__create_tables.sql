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

ALTER TABLE schedule
    ADD COLUMN university_id BIGINT REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE audiences
    ADD COLUMN number INT UNIQUE,
    ADD COLUMN capacity      INT    ,
    ADD COLUMN university_id BIGINT  REFERENCES university (id) ON
DELETE
CASCADE;

ALTER TABLE faculties
    ADD COLUMN name varchar(256) UNIQUE,
    ADD COLUMN university_id BIGINT        REFERENCES university (id) ON
DELETE
CASCADE;

ALTER TABLE departments
    ADD COLUMN name varchar(256) UNIQUE,
    ADD COLUMN faculty_id    BIGINT        REFERENCES faculties (id) ON
DELETE
CASCADE,
    ADD COLUMN university_id BIGINT        REFERENCES university (id) ON DELETE
CASCADE;

ALTER TABLE lectures
    ADD COLUMN number INT ,
    ADD COLUMN date        DATE   ,
    ADD COLUMN audience_id BIGINT  REFERENCES audiences (id) ON
DELETE
SET NULL,
    ADD COLUMN lesson_id   BIGINT  REFERENCES lessons (id) ON DELETE
SET NULL,
    ADD COLUMN teacher_id  BIGINT  REFERENCES teachers (id) ON DELETE
SET NULL,
    ADD COLUMN schedule_id BIGINT  REFERENCES schedule (id) ON DELETE
CASCADE;

ALTER TABLE groups
    ADD COLUMN name varchar(256) UNIQUE,
    ADD COLUMN lecture_id    BIGINT        REFERENCES lectures (id) ON
DELETE
SET NULL,
    ADD COLUMN department_id BIGINT        REFERENCES departments (id) ON DELETE
SET NULL,
    ADD COLUMN faculty_id    BIGINT        REFERENCES faculties (id) ON DELETE
CASCADE,
    ADD COLUMN university_id BIGINT        REFERENCES university (id) ON DELETE
CASCADE;

ALTER TABLE students
    ADD COLUMN first_name varchar(50),
    ADD COLUMN last_name     varchar(50),
    ADD COLUMN middle_name   varchar(50),
    ADD COLUMN course_number INT         ,
    ADD COLUMN group_id      BIGINT      REFERENCES groups (id) ON
DELETE
SET NULL,
    ADD COLUMN faculty_id    BIGINT      REFERENCES faculties (id) ON DELETE
SET NULL,
    ADD COLUMN university_id BIGINT       REFERENCES university (id) ON DELETE
CASCADE;

ALTER TABLE lessons
    ADD COLUMN number INT ,
    ADD COLUMN start_time TIME     ,
    ADD COLUMN duration   INTERVAL,
    ADD COLUMN subject_id BIGINT    REFERENCES subjects (id) ON
DELETE
SET NULL;

ALTER TABLE subjects
    ADD COLUMN name varchar(256) UNIQUE,
    ADD COLUMN university_id BIGINT        REFERENCES university (id) ON
DELETE
CASCADE;

ALTER TABLE teachers
    ADD COLUMN first_name varchar(50) ,
    ADD COLUMN last_name     varchar(50) ,
    ADD COLUMN middle_name   varchar(50) ,
    ADD COLUMN student_id    BIGINT       REFERENCES students (id) ON
DELETE
SET NULL,
    ADD COLUMN university_id BIGINT       REFERENCES university (id) ON DELETE
CASCADE;

create table subjects_teachers
(
    subject_id BIGINT REFERENCES subjects (id) ON DELETE CASCADE,
    teacher_id BIGINT REFERENCES teachers (id) ON DELETE CASCADE,
    PRIMARY KEY (subject_id, teacher_id)
);

create table subjects_students
(
    subject_id BIGINT REFERENCES subjects (id) ON DELETE CASCADE,
    student_id BIGINT REFERENCES students (id) ON DELETE CASCADE,
    PRIMARY KEY (subject_id, student_id)
);