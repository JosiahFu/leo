CREATE TABLE class
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name              VARCHAR(255) NOT NULL,
    short_descr       TEXT         NOT NULL,
    short_descr_quill TEXT         NOT NULL,
    long_descr        TEXT         NOT NULL,
    long_descr_quill  TEXT         NOT NULL,

    school_id         INT          NOT NULL,
    CONSTRAINT class_school_id
        FOREIGN KEY (school_id)
            REFERENCES school (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
