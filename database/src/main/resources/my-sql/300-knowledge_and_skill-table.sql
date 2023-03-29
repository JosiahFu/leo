CREATE TABLE knowledge_and_skill
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    name              VARCHAR(255)  NOT NULL,
    short_descr_quill BLOB          NOT NULL,
    long_descr_quill  LONGBLOB      NOT NULL,
    -- JSON array of strings: ["mastery_1", "mastery_2", ...]
    mastery           VARCHAR(1024) NOT NULL,

    class_id          INT           NOT NULL,
    CONSTRAINT knowledge_and_skill_class_id
        FOREIGN KEY (class_id)
            REFERENCES class (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
