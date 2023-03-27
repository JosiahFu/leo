CREATE TABLE admins
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,

    district_id BIGINT NOT NULL,
    CONSTRAINT admins_district_id
        FOREIGN KEY (district_id)
            REFERENCES districts (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
