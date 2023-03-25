CREATE TABLE projects
(
    id             BIGINT PRIMARY KEY AUTO_INCREMENT,

    title          VARCHAR(255) NOT NULL,
    short_descr    VARCHAR(255) NOT NULL,
    start_time_utc Timestamp    NOT NULL,
    love           VARCHAR(255) NOT NULL,
    need           VARCHAR(255) NOT NULL,
    paid           VARCHAR(255) NOT NULL,

    assignment_id  BIGINT       NOT NULL,
    CONSTRAINT projects_assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignments (id)
            ON UPDATE RESTRICT,

    student_id     BIGINT       NOT NULL,
    CONSTRAINT projects_student_id
        FOREIGN KEY (student_id)
            REFERENCES students (id)
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
