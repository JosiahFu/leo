CREATE TABLE project_definition
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    -- Whether this is a template that a teacher could select from for an assignment.
    template      BOOLEAN,
    template_name VARCHAR(255)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
