CREATE OR ALTER TABLE users(
    id INT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email_address VARCHAR(255) NOT NULL,
    -- sha256: UTF-8 password, id, host.
    sha256_password CHAR(64) NOT NULL,
    PRIMARY KEY (id));
