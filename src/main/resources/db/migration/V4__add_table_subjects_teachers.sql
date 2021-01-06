create table subjects_teachers
(
    subject_id BIGINT REFERENCES subjects (id) ON DELETE CASCADE,
    teacher_id BIGINT REFERENCES teachers (id) ON DELETE CASCADE,
    PRIMARY KEY (subject_id, teacher_id)
);