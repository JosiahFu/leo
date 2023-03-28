CREATE TABLE class_teacher
(
    class_id   INT NOT NULL,
    INDEX (class_id),
    CONSTRAINT class_teacher_class_id
        FOREIGN KEY (class_id)
            REFERENCES class (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    teacher_id INT NOT NULL,
    INDEX (teacher_id),
    CONSTRAINT class_teacher_teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teacher (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (class_id, teacher_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
