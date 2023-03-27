CREATE TABLE project_posts
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,

    title            VARCHAR(255) NOT NULL,
    short_descr      VARCHAR(255) NOT NULL,
    post_time_utc    Timestamp    NOT NULL,

    project_cycle_id BIGINT       NOT NULL,
    CONSTRAINT project_posts_project_cycle_id
        FOREIGN KEY (project_cycle_id)
            REFERENCES project_cycles (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
