CREATE TABLE student_class
(
    student_id INT NOT NULL,
    CONSTRAINT student_class_student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    class_id   INT NOT NULL,
    CONSTRAINT student_class_class_id
        FOREIGN KEY (class_id)
            REFERENCES class (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (student_id, class_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
