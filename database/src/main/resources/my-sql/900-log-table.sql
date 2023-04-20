CREATE TABLE log
(
    id                        INT PRIMARY KEY AUTO_INCREMENT,
    -- This is also effectively the request time.
    creation_time             DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,

    source                    VARCHAR(1024) NOT NULL,
    notes                     MEDIUMTEXT,
    request                   MEDIUMTEXT    NOT NULL,
    unprocessed_response      MEDIUMBLOB,
    unprocessed_response_time DATETIME,
    response                  MEDIUMTEXT,
    status                    MEDIUMTEXT,

    user_id                   INT,
    CONSTRAINT log_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
