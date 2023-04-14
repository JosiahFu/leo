CREATE TABLE log_reference
(
    id              INT PRIMARY KEY AUTO_INCREMENT,
    creation_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    log_id          INT      NOT NULL,
    CONSTRAINT log_reference_log_id
        FOREIGN KEY (log_id)
            REFERENCES log (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    ikigai_input_id INT,
    CONSTRAINT log_reference_ikigai_input_id
        FOREIGN KEY (ikigai_input_id)
            REFERENCES ikigai_input (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_id      INT,
    CONSTRAINT log_reference_project_id
        FOREIGN KEY (project_id)
            REFERENCES project (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
