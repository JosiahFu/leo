CREATE TABLE student
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    student_id    VARCHAR(20) NOT NULL,
    grade         VARCHAR(20)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
