CREATE TABLE class
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    name              VARCHAR(255) NOT NULL,
    short_descr_quill BLOB         NOT NULL,
    long_descr_quill  LONGBLOB     NOT NULL,

    school_id         INT          NOT NULL,
    CONSTRAINT class_school_id
        FOREIGN KEY (school_id)
            REFERENCES school (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
