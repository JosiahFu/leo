CREATE TABLE knowledge_and_skills
(
    id          BIGINT PRIMARY KEY AUTO_INCREMENT,

    title       VARCHAR(255) NOT NULL,
    short_descr VARCHAR(255) NOT NULL,
    -- JSON array of strings: ["mastery_1", "mastery_2", ...]
    mastery   VARCHAR(1024) NOT NULL,

    project_id  BIGINT NOT NULL,
    CONSTRAINT knowledge_and_skills_project_id
        FOREIGN KEY (project_id)
            REFERENCES projects (id)
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
