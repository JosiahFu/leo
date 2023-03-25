CREATE TABLE project_cycles
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,

    title          VARCHAR(255) NOT NULL,
    short_descr    VARCHAR(255) NOT NULL,
    start_time_utc Timestamp NOT NULL,

    project_id     BIGINT       NOT NULL,
    CONSTRAINT project_cycles_project_id
        FOREIGN KEY (project_id)
            REFERENCES projects (id)
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
