CREATE TABLE school
(
    id          INT PRIMARY KEY AUTO_INCREMENT,

    name        VARCHAR(255) NOT NULL,
    city        VARCHAR(255) NOT NULL,

    district_id INT          NOT NULL,
    CONSTRAINT school_district_id
        FOREIGN KEY (district_id)
            REFERENCES district (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
