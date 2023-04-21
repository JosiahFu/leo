CREATE TABLE student
(
    id            INT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    student_id    INT      NOT NULL,
    grade         INT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
