CREATE TABLE teacher_school
(
    teacher_id INT NOT NULL,
    INDEX (teacher_id),
    CONSTRAINT teacher_school_teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teacher (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    school_id  INT NOT NULL,
    INDEX (school_id),
    CONSTRAINT teacher_school_school_id
        FOREIGN KEY (school_id)
            REFERENCES school (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (teacher_id, school_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
