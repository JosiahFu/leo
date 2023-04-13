CREATE TABLE project_cycle
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,

    title                 VARCHAR(255) NOT NULL,
    short_desc            VARCHAR(2048)  NOT NULL,
    short_descr_quill_zip BLOB,
    long_desc             VARCHAR(16384) NOT NULL,
    long_descr_quill_zip  BLOB,

    start_time_micros_utc BIGINT       NOT NULL,

    project_id            INT          NOT NULL,
    CONSTRAINT project_cycle_project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
