CREATE TABLE classes_teachers
(
    class_id   INT NOT NULL,
    INDEX (class_id),
    CONSTRAINT classes_teachers_class_id
        FOREIGN KEY (class_id)
            REFERENCES classes (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    teacher_id INT NOT NULL,
    INDEX (teacher_id),
    CONSTRAINT classes_teachers_teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teachers (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (class_id, teacher_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
