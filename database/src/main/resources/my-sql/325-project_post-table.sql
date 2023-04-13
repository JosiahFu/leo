CREATE TABLE project_post
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    post_time_micros_utc  BIGINT        NOT NULL,

    name                  VARCHAR(255)  NOT NULL,
    short_descr           VARCHAR(2048) NOT NULL,
    short_descr_quill_zip BLOB,
    long_descr            TEXT          NOT NULL,
    long_descr_quill_zip  BLOB,

    user_id               INT           NOT NULL,
    CONSTRAINT project_post_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_id            INT           NOT NULL,
    CONSTRAINT project_post_project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
