CREATE TABLE projects
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,

    title                 VARCHAR(255) NOT NULL,
    short_descr_quill     BLOB         NOT NULL,
    long_descr_quill      LONGBLOB     NOT NULL,

    love                  VARCHAR(255) NOT NULL,
    need                  VARCHAR(255) NOT NULL,
    paid                  VARCHAR(255) NOT NULL,

    start_time_micros_utc BIGINT       NOT NULL,

    assignment_id         INT          NOT NULL,
    CONSTRAINT projects_assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignments (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id            INT          NOT NULL,
    CONSTRAINT projects_student_id
        FOREIGN KEY (student_id)
            REFERENCES students (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
