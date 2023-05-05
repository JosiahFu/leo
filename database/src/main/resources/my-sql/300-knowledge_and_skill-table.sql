CREATE TABLE knowledge_and_skill
(
    id                INT PRIMARY KEY AUTO_INCREMENT,
    creation_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name              VARCHAR(255) NOT NULL,
    type              ENUM ('EKS', 'XQ_COMPETENCY') NOT NULL,
    short_descr       TEXT,
    short_descr_quill TEXT,
    long_descr        TEXT,
    long_descr_quill  TEXT,

    class_x_id        INT          NOT NULL,
    CONSTRAINT knowledge_and_skill__class_x_id
        FOREIGN KEY (class_x_id)
            REFERENCES class_x (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
