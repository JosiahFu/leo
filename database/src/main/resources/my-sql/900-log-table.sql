CREATE TABLE log
(
    id                        INT PRIMARY KEY AUTO_INCREMENT,
    creation_time             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    source                    VARCHAR(1024) NOT NULL,
    notes                     MEDIUMTEXT,
    request                   MEDIUMTEXT    NOT NULL,
    request_time              DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    unprocessed_response      MEDIUMBLOB,
    unprocessed_response_time DATETIME,
    response                  MEDIUMTEXT,
    response_time             DATETIME,
    status                    MEDIUMTEXT,

    user_id                   INT,
    CONSTRAINT log_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
