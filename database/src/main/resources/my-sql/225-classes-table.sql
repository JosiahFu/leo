CREATE TABLE classes
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,

    title       VARCHAR(255) NOT NULL,
    short_descr VARCHAR(255) NOT NULL,

    school_id   BIGINT       NOT NULL,
    CONSTRAINT classes_school_id
        FOREIGN KEY (school_id)
            REFERENCES schools (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
