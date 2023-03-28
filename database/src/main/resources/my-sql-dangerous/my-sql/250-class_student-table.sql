CREATE TABLE class_student
(
    class_id   INT NOT NULL,
    INDEX (class_id),
    CONSTRAINT class_student_class_id
        FOREIGN KEY (class_id)
            REFERENCES class (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id INT NOT NULL,
    INDEX (student_id),
    CONSTRAINT class_student_student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (class_id, student_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
