CREATE TABLE classes_students
(
    class_id   INT NOT NULL,
    INDEX (class_id),
    CONSTRAINT classes_students_class_id
        FOREIGN KEY (class_id)
            REFERENCES classes (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id INT NOT NULL,
    INDEX (student_id),
    CONSTRAINT classes_students_student_id
        FOREIGN KEY (student_id)
            REFERENCES students (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (class_id, student_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
