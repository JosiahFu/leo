CREATE TABLE knowledge_and_skill_assignment
(
    creation_time          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,

    knowledge_and_skill_id INT      NOT NULL,
    CONSTRAINT knowledge_and_skill_assignment_knowledge_and_skill_id
        FOREIGN KEY (knowledge_and_skill_id)
            REFERENCES knowledge_and_skill (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    assignment_id          INT      NOT NULL,
    CONSTRAINT knowledge_and_skill_assignment_assignment_id
        FOREIGN KEY (assignment_id)
            REFERENCES assignment (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    PRIMARY KEY (knowledge_and_skill_id, assignment_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
