CREATE TABLE users
(
    id                   BIGINT              NOT NULL AUTO_INCREMENT,
    first_name           VARCHAR(255)        NOT NULL,
    last_name            VARCHAR(255)        NOT NULL,
    role                 ENUM ('student', 'teacher', 'admin') NOT NULL,
    -- https://www.rfc-editor.org/errata_search.php?rfc=3696&eid=1690
    email_address        VARCHAR(320) UNIQUE NOT NULL,
    -- sha256 of UTF-8 password bytes, id bytes, UTF-8 salt bytes.
    sha256_password_hash BINARY(32) NOT NULL,
    PRIMARY KEY (id)
) CHAR SET `UTF8`;
