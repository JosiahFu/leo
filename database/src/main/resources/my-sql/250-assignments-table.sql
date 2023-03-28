CREATE TABLE assignments
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    title             VARCHAR(255) NOT NULL,
    short_descr_quill BLOB         NOT NULL,
    long_descr_quill  LONGBLOB     NOT NULL,

    class_id          INT          NOT NULL,
    CONSTRAINT assignments_class_id
        FOREIGN KEY (class_id)
            REFERENCES classes (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
