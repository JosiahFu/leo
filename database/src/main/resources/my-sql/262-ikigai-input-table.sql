CREATE TABLE ikigai_input
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    creation_time        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    something_you_love   TEXT     NOT NULL,
    what_you_are_good_at TEXT     NOT NULL,
    pending_completion   DATETIME,

    assignment_id        INT      NOT NULL,
    CONSTRAINT ikigai_input__assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    user_x_id            INT      NOT NULL,
    CONSTRAINT ikigai_input__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
