CREATE TABLE project_input
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    creation_time        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    something_you_love   TEXT,
    what_you_are_good_at TEXT,

    pending_completion   DATETIME,

    assignment_id        INT,
    CONSTRAINT project_input__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id           INT      NOT NULL,
    CONSTRAINT project_input__student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
