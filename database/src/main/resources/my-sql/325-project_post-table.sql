CREATE TABLE project_post
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,

    name                 VARCHAR(255) NOT NULL,
    short_descr_quill    BLOB         NOT NULL,
    long_descr_quill     LONGBLOB     NOT NULL,

    post_time_micros_utc BIGINT       NOT NULL,

    user_id              INT          NOT NULL,
    CONSTRAINT project_post_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
