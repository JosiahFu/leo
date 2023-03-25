CREATE TABLE assignments
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,

    title       VARCHAR(255) NOT NULL,
    short_descr VARCHAR(255) NOT NULL,

    class_id    BIGINT       NOT NULL,
    CONSTRAINT assignments_class_id
        FOREIGN KEY (class_id)
            REFERENCES classes (id)
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
