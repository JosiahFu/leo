CREATE TABLE users
(
    id               BIGINT              NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (id),

    first_name       VARCHAR(255)        NOT NULL,
    last_name        VARCHAR(255)        NOT NULL,
    -- https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
    email_address    VARCHAR(254) UNIQUE NOT NULL,
    -- First byte is the hash version.
    encoded_password BLOB                NOT NULL,

    teacher_id       BIGINT,
    CONSTRAINT users_teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teachers (id)
            ON DELETE SET NULL
            ON UPDATE RESTRICT,

    student_id       BIGINT,
    CONSTRAINT users_student_id
        FOREIGN KEY (student_id)
            REFERENCES students (id)
            ON DELETE SET NULL
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
