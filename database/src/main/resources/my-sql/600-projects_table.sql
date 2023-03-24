CREATE TABLE projects
(
    project_id    BIGINT              NOT NULL AUTO_INCREMENT,
    user_id       BIGINT              NOT NULL,
    class_id      BIGINT,
    title         VARCHAR(255)        NOT NULL,
    short_descr   VARCHAR(255)        NOT NULL,
    love          VARCHAR(255)        NOT NULL,
    need          VARCHAR(255)        NOT NULL,
    paid          VARCHAR(255)        NOT NULL,
    eks_id        BIGINT              NOT NULL,
    length_hours  INT,   
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (class_id) REFERENCES users(id),
    FOREIGN KEY (eks_id) REFERENCES knowledge_and_skills(eks_id),
    PRIMARY KEY (project_id)
) CHAR SET `UTF8MB4`;
