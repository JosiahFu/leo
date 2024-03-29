CREATE TABLE project_post_comment
(
    id              INT PRIMARY KEY AUTO_INCREMENT,

    comment_quill   LONGBLOB NOT NULL,

    order_index     INT      NOT NULL,

    user_id         INT      NOT NULL,
    CONSTRAINT project_post_comment_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id INT      NOT NULL,
    INDEX (project_post_id),
    CONSTRAINT project_post_comment_project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
