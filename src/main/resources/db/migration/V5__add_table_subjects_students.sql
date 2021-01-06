create table subjects_students
(
    subject_id BIGINT REFERENCES subjects (id) ON DELETE CASCADE,
    student_id BIGINT REFERENCES students (id) ON DELETE CASCADE,
    PRIMARY KEY (subject_id, student_id)
);