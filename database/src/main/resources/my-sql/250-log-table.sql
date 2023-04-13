CREATE TABLE log
(
    id                   INT PRIMARY KEY AUTO_INCREMENT,

    timestamp_micros_utc BIGINT        NOT NULL,
    source               VARCHAR(1024) NOT NULL,

    notes                MEDIUMTEXT    NOT NULL,
    request              MEDIUMTEXT    NOT NULL,
    response             MEDIUMTEXT    NOT NULL,

    user_id              INT           NOT NULL,
    CONSTRAINT log_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
