CREATE TABLE ikigai_input
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,
    creation_time        DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    something_you_love   VARCHAR(255) NOT NULL,
    what_you_are_good_at VARCHAR(255) NOT NULL,
    pending_completion   DATETIME,

    assignment_id        INT          NOT NULL,
    CONSTRAINT ikigai_input_assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    user_id              INT          NOT NULL,
    CONSTRAINT ikigai_input_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
