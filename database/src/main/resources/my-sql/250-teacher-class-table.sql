CREATE TABLE teacher_class
(
    teacher_id INT NOT NULL,
    INDEX (teacher_id),
    CONSTRAINT teacher_class_teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teacher (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    class_id   INT NOT NULL,
    INDEX (class_id),
    CONSTRAINT teacher_class_class_id
        FOREIGN KEY (class_id)
            REFERENCES class (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (teacher_id, class_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
