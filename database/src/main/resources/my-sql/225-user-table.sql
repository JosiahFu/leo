CREATE TABLE user
(
    id               INT PRIMARY KEY AUTO_INCREMENT,
    creation_time    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP,

    first_name       VARCHAR(255)        NOT NULL,
    last_name        VARCHAR(255)        NOT NULL,
    -- https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
    email_address    VARCHAR(254) UNIQUE NOT NULL,
    -- From org.springframework.security.crypto.factory.PasswordEncoderFactories.
    encoded_password TEXT                NOT NULL,

    district_id      INT,
    CONSTRAINT user_district_id
        FOREIGN KEY (district_id)
            REFERENCES district (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    admin_id         INT UNIQUE,
    CONSTRAINT user_admin_id
        FOREIGN KEY (admin_id)
            REFERENCES admin (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    teacher_id       INT UNIQUE,
    CONSTRAINT user_teacher_id
        FOREIGN KEY (teacher_id)
            REFERENCES teacher (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id       INT UNIQUE,
    CONSTRAINT user_student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
