CREATE TABLE project_cycle_step
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Indicates the relative location on the cycle.
    position          INT          NOT NULL,

    name              VARCHAR(255) NOT NULL,
    short_descr       TEXT,
    short_descr_quill TEXT,
    long_descr        TEXT,
    long_descr_quill  TEXT,

    project_cycle_id  INT          NOT NULL,
    CONSTRAINT project_cycle_step__project_cycle_id
        FOREIGN KEY (project_cycle_id)
            REFERENCES project_cycle (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
