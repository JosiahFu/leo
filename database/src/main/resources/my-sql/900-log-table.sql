CREATE TABLE log
(
    id                    INT PRIMARY KEY AUTO_INCREMENT,
    -- This is also effectively the request time.
    creation_time         DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    operation             VARCHAR(511) NOT NULL,
    request               MEDIUMTEXT   NOT NULL,
    -- The time of a response from external services.
    initial_response      MEDIUMBLOB,
    initial_response_time DATETIME,
    -- The time of a fully processed response to send back to the client.
    final_response        MEDIUMTEXT,
    final_response_time   DATETIME,
    stack_trace           MEDIUMTEXT,
    status                ENUM('ERROR', 'SUCCESS') NOT NULL,

    notes                 MEDIUMTEXT,

    user_id               INT,
    CONSTRAINT log_user_id
        FOREIGN KEY (user_id)
            REFERENCES user (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
