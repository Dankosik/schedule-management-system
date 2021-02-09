create table university
(
    id BIGSERIAL NOT NULL PRIMARY KEY
);

insert into university(id) values (1);

create table subjects
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    name          varchar(256) UNIQUE,
    university_id BIGINT REFERENCES university (id) ON DELETE CASCADE
);

create table audiences
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    number        INT UNIQUE,
    capacity      INT,
    university_id BIGINT REFERENCES university (id) ON DELETE CASCADE
);

create table lessons
(
    id         BIGSERIAL NOT NULL PRIMARY KEY,
    number     INT,
    start_time TIME,
    duration   INTERVAL,
    subject_id BIGINT    REFERENCES subjects (id) ON DELETE SET NULL
);


create table faculties
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    name          varchar(256) UNIQUE,
    university_id BIGINT REFERENCES university (id) ON DELETE CASCADE
);

create table teachers
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    first_name    varchar(50),
    last_name     varchar(50),
    middle_name   varchar(50),
    faculty_id    BIGINT REFERENCES faculties (id) ON DELETE CASCADE,
    university_id BIGINT REFERENCES university (id) ON DELETE CASCADE
);

create table lectures
(
    id          BIGSERIAL NOT NULL PRIMARY KEY,
    number      INT,
    date        DATE,
    audience_id BIGINT    REFERENCES audiences (id) ON DELETE SET NULL,
    lesson_id   BIGINT    REFERENCES lessons (id) ON DELETE SET NULL,
    teacher_id  BIGINT    REFERENCES teachers (id) ON DELETE SET NULL
);
create table groups
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    name          varchar(256) UNIQUE,
    faculty_id    BIGINT REFERENCES faculties (id) ON DELETE CASCADE,
    university_id BIGINT REFERENCES university (id) ON DELETE CASCADE
);

create table students
(
    id            BIGSERIAL NOT NULL PRIMARY KEY,
    first_name    varchar(50),
    last_name     varchar(50),
    middle_name   varchar(50),
    course_number INT,
    group_id      BIGINT    REFERENCES groups (id) ON DELETE SET NULL,
    university_id BIGINT REFERENCES university (id) ON DELETE CASCADE
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
    student_id BIGINT REFERENCES students (id) ON DELETE CASCADE,
    PRIMARY KEY (subject_id, student_id)
);
