CREATE TABLE project
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name                  VARCHAR(255)  NOT NULL,
    short_descr           VARCHAR(2048) NOT NULL,
    short_descr_quill_zip BLOB,
    long_descr            TEXT          NOT NULL,
    long_descr_quill_zip  BLOB,

    ikigai_input_id       INT           NOT NULL,
    CONSTRAINT project_ikigai_input_id
        FOREIGN KEY (ikigai_input_id)
            REFERENCES ikigai_input (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
