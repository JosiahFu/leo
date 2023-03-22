CREATE TABLE users
(
    id            BIGINT              NOT NULL AUTO_INCREMENT,
    first_name    VARCHAR(255)        NOT NULL,
    last_name     VARCHAR(255)        NOT NULL,
    role          ENUM ('STUDENT', 'TEACHER', 'ADMIN') NOT NULL,
    -- https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
    email_address VARCHAR(254) UNIQUE NOT NULL,
    -- First byte is the hash version.
    password_hash BINARY(65) NOT NULL,
    PRIMARY KEY (id)
) CHAR SET `UTF8MB4`;
