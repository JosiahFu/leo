CREATE TABLE project_cycle
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name              VARCHAR(255) NOT NULL,
    short_descr       TEXT,
    short_descr_quill TEXT,
    long_descr        TEXT,
    long_descr_quill  TEXT,

    project_id        INT          NOT NULL,
    CONSTRAINT project_cycle__project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
