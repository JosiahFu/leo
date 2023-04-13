CREATE TABLE project
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,

    title                 VARCHAR(255) NOT NULL,
    short_desc            VARCHAR(2048)  NOT NULL,
    short_descr_quill_zip BLOB,
    long_desc             VARCHAR(16384) NOT NULL,
    long_descr_quill_zip  BLOB,

    love                  VARCHAR(255) NOT NULL,
    need                  VARCHAR(255) NOT NULL,
    paid                  VARCHAR(255) NOT NULL,

    start_time_micros_utc BIGINT       NOT NULL,

    assignment_id         INT          NOT NULL,
    CONSTRAINT project_assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id            INT          NOT NULL,
    CONSTRAINT project_student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
