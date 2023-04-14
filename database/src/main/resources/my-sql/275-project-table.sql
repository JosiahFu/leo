CREATE TABLE project
(
    id                          INT PRIMARY KEY AUTO_INCREMENT,
    creation_time               DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name                        VARCHAR(255)  NOT NULL,
    short_descr                 VARCHAR(2048) NOT NULL,
    short_descr_quill_zip       BLOB,
    long_descr                  TEXT          NOT NULL,
    long_descr_quill_zip        BLOB,

    love_descr                  TEXT          NOT NULL,
    love_descr_quill_zip        BLOB,
    world_needs_descr           TEXT          NOT NULL,
    world_needs_descr_quill_zip BLOB,
    paid_for_descr              TEXT          NOT NULL,
    paid_for_descr_quill_zip    BLOB,
    good_at_descr               TEXT          NOT NULL,
    good_at_descr_quill_zip     BLOB,

    assignment_id               INT           NOT NULL,
    CONSTRAINT project_assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id                  INT           NOT NULL,
    CONSTRAINT project_student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
