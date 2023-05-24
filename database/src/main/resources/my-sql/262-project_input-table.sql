CREATE TABLE project_input
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    timeout               DATETIME,
    state                 ENUM ('PROCESSING', 'COMPLETED', 'FAILED') NOT NULL,

    project_definition_id INT,
    CONSTRAINT project_input__project_definition_id
        FOREIGN KEY (project_definition_id)
            REFERENCES project_definition (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    student_id            INT      NOT NULL,
    CONSTRAINT project_input__student_id
        FOREIGN KEY (student_id)
            REFERENCES student (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
