CREATE TABLE class
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,

    title                 VARCHAR(255)  NOT NULL,
    short_desc            VARCHAR(2048)  NOT NULL,
    short_descr_quill_zip BLOB,
    long_desc             VARCHAR(16384) NOT NULL,
    long_descr_quill_zip  BLOB,

    school_id             INT           NOT NULL,
    CONSTRAINT class_school_id
        FOREIGN KEY (school_id)
            REFERENCES school (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
