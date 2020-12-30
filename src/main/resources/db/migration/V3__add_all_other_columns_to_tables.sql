ALTER TABLE university
    ADD COLUMN schedule_id BIGINT NOT NULL REFERENCES schedule (id) ON DELETE CASCADE;

ALTER TABLE audience
    ADD COLUMN number        INT    NOT NULL,
    ADD COLUMN capacity      INT    NOT NULL,
    ADD COLUMN university_id BIGINT NOT NULL REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE faculties
    ADD COLUMN name          varchar(256) NOT NULL,
    ADD COLUMN university_id BIGINT       NOT NULL REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE departments
    ADD COLUMN name          varchar(256) NOT NULL,
    ADD COLUMN faculty_id    BIGINT       NOT NULL REFERENCES faculties (id) ON DELETE CASCADE,
    ADD COLUMN university_id BIGINT       NOT NULL REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE lectures
    ADD COLUMN number      INT    NOT NULL,
    ADD COLUMN date        DATE   NOT NULL,
    ADD COLUMN audience_id BIGINT NOT NULL REFERENCES audience (id) ON DELETE CASCADE,
    ADD COLUMN lesson_id   BIGINT NOT NULL REFERENCES lessons (id) ON DELETE CASCADE,
    ADD COLUMN teacher_id  BIGINT NOT NULL REFERENCES teachers (id) ON DELETE CASCADE,
    ADD COLUMN schedule_id BIGINT NOT NULL REFERENCES schedule (id) ON DELETE CASCADE;

ALTER TABLE groups
    ADD COLUMN name          varchar(256) NOT NULL,
    ADD COLUMN lecture_id    BIGINT       NOT NULL REFERENCES lectures (id) ON DELETE CASCADE,
    ADD COLUMN department_id BIGINT       NOT NULL REFERENCES departments (id) ON DELETE CASCADE,
    ADD COLUMN faculty_id    BIGINT       NOT NULL REFERENCES faculties (id) ON DELETE CASCADE,
    ADD COLUMN university_id BIGINT       NOT NULL REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE students
    ADD COLUMN first_name    varchar(50) NOT NULL,
    ADD COLUMN last_name     varchar(50) NOT NULL,
    ADD COLUMN middle_name   varchar(50) NOT NULL,
    ADD COLUMN course_number INT         NOT NULL,
    ADD COLUMN group_id      BIGINT      NOT NULL REFERENCES groups (id) ON DELETE CASCADE,
    ADD COLUMN faculty_id    BIGINT      NOT NULL REFERENCES faculties (id) ON DELETE CASCADE,
    ADD COLUMN university_id BIGINT      NOT NULL REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE lessons
    ADD COLUMN number     INT      NOT NULL,
    ADD COLUMN start_time TIME     NOT NULL,
    ADD COLUMN duration   INTERVAL NOT NULL,
    ADD COLUMN subject_id BIGINT   NOT NULL REFERENCES subjects (id) ON DELETE CASCADE;

ALTER TABLE subjects
    ADD COLUMN name          varchar(256) NOT NULL,
    ADD COLUMN student_id    BIGINT       NOT NULL REFERENCES students (id) ON DELETE CASCADE,
    ADD COLUMN teacher_id    BIGINT       NOT NULL REFERENCES teachers (id) ON DELETE CASCADE,
    ADD COLUMN university_id BIGINT       NOT NULL REFERENCES university (id) ON DELETE CASCADE;

ALTER TABLE teachers
    ADD COLUMN first_name    varchar(50) NOT NULL,
    ADD COLUMN last_name     varchar(50) NOT NULL,
    ADD COLUMN middle_name   varchar(50) NOT NULL,
    ADD COLUMN student_id    BIGINT      NOT NULL REFERENCES students (id) ON DELETE CASCADE,
    ADD COLUMN university_id BIGINT      NOT NULL REFERENCES university (id) ON DELETE CASCADE;