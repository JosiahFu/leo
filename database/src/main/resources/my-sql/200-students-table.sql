CREATE TABLE students
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,

    school_id   BIGINT       NOT NULL,
    CONSTRAINT students_school_id
        FOREIGN KEY (school_id)
            REFERENCES schools (id)
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
