CREATE TABLE portfolio
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name              VARCHAR(255) NOT NULL,
    short_descr       TEXT         NOT NULL,
    short_descr_quill TEXT         NOT NULL,
    long_descr        TEXT         NOT NULL,
    long_descr_quill  TEXT         NOT NULL
) ENGINE InnoDB
  CHAR SET UTF8MB4;
