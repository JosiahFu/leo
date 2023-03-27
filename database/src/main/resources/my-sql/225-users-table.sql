CREATE TABLE users
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,

    first_name       VARCHAR(255)        NOT NULL,
    last_name        VARCHAR(255)        NOT NULL,
    -- https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
    email_address    VARCHAR(254) UNIQUE NOT NULL,
    -- First byte is the hash version.
    encoded_password BLOB                NOT NULL,

    admin_id       BIGINT UNIQUE,
    CONSTRAINT users_admin_id
        FOREIGN KEY (admin_id)
            REFERENCES admins (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    teacher_id       BIGINT UNIQUE,
    CONSTRAINT users_teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teachers (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id       BIGINT UNIQUE,
    CONSTRAINT users_student_id
        FOREIGN KEY (student_id)
            REFERENCES students (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
