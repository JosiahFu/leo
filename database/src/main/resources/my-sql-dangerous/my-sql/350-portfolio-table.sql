CREATE TABLE portfolio
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    title             VARCHAR(255) NOT NULL,
    short_desc            VARCHAR(2048)  NOT NULL,
    short_descr_quill_zip BLOB,
    long_desc             VARCHAR(16384) NOT NULL,
    long_descr_quill_zip  BLOB,
) ENGINE InnoDB
  CHAR SET UTF8MB4;
