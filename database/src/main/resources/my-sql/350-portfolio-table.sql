CREATE TABLE portfolio
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    creation_time         DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name                  VARCHAR(255)  NOT NULL,
    short_descr           VARCHAR(2048) NOT NULL,
    short_descr_quill_zip BLOB,
    long_descr            TEXT          NOT NULL,
    long_descr_quill_zip  BLOB
) ENGINE InnoDB
  CHAR SET UTF8MB4;
