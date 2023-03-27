CREATE TABLE teachers_schools
(
    teacher_id   BIGINT NOT NULL,
    INDEX (teacher_id) USING HASH,
    CONSTRAINT teachers_schools_teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teachers (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    school_id BIGINT NOT NULL,
    INDEX (school_id) USING HASH,
    CONSTRAINT teachers_schools_school_id
        FOREIGN KEY (school_id)
            REFERENCES schools (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (teacher_id, school_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
