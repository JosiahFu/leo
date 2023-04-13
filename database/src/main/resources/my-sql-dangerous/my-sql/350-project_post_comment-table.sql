CREATE TABLE project_post_comment
(
    id              INT PRIMARY KEY AUTO_INCREMENT,

    short_desc            VARCHAR(2048)  NOT NULL,
    short_descr_quill_zip BLOB,
    long_desc             VARCHAR(16384) NOT NULL,
    long_descr_quill_zip  BLOB,

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
