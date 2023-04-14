CREATE TABLE project_cycle
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name                  VARCHAR(255)  NOT NULL,
    short_descr           VARCHAR(2048) NOT NULL,
    short_descr_quill_zip BLOB,
    long_descr            TEXT          NOT NULL,
    long_descr_quill_zip  BLOB,

    project_id            INT           NOT NULL,
    CONSTRAINT project_cycle_project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
