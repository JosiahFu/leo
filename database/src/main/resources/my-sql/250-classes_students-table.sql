CREATE TABLE classes_students
(
    class_id   BIGINT NOT NULL,
    INDEX (class_id) USING HASH,
    CONSTRAINT classes_students_class_id
        FOREIGN KEY (class_id)
            REFERENCES classes (id)
            ON UPDATE RESTRICT,

    student_id BIGINT NOT NULL,
    INDEX (student_id) USING HASH,
    CONSTRAINT classes_students_student_id
        FOREIGN KEY (student_id)
            REFERENCES students (id)
            ON UPDATE RESTRICT,

    PRIMARY KEY (class_id, student_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
