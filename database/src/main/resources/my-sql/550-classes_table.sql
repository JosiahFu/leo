CREATE TABLE classes
(
    class_id      BIGINT              NOT NULL AUTO_INCREMENT,
    school_id     BIGINT              NOT NULL,
    title         VARCHAR(255)        NOT NULL,
    short_descr   VARCHAR(255)        NOT NULL,
    district      VARCHAR(255)        NOT NULL,
    city          VARCHAR(255)        NOT NULL,  
    FOREIGN KEY (school_id) REFERENCES schools(school_id),
    PRIMARY KEY (class_id)
) CHAR SET `UTF8MB4`;
