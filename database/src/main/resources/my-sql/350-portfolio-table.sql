CREATE TABLE portfolio
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    name              VARCHAR(255) NOT NULL,
    short_descr_quill BLOB         NOT NULL,
    long_descr_quill  LONGBLOB     NOT NULL
) ENGINE InnoDB
  CHAR SET UTF8MB4;
