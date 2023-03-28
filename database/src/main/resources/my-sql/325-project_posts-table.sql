CREATE TABLE project_posts
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,

    title                VARCHAR(255) NOT NULL,
    short_descr_quill    BLOB         NOT NULL,
    long_descr_quill     LONGBLOB     NOT NULL,

    post_time_micros_utc BIGINT       NOT NULL,

    user_id              INT          NOT NULL,
    CONSTRAINT project_posts_comments_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
