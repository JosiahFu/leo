CREATE TABLE project_post
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name              VARCHAR(255) NOT NULL,
    short_descr       TEXT,
    short_descr_quill TEXT,
    long_descr        TEXT,
    long_descr_quill  TEXT,

    user_x_id         INT          NOT NULL,
    CONSTRAINT project_post__user_x_id
        FOREIGN KEY (user_x_id)
            REFERENCES user_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_id        INT          NOT NULL,
    CONSTRAINT project_post__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
