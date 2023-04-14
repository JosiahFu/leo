CREATE TABLE project_post_comment
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    comment           TEXT     NOT NULL,
    comment_quill_zip BLOB,

    user_id           INT      NOT NULL,
    CONSTRAINT project_post_comment_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id   INT      NOT NULL,
    CONSTRAINT project_post_comment_project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
