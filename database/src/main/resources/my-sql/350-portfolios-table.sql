CREATE TABLE portfolios
(
    id                BIGINT PRIMARY KEY AUTO_INCREMENT
#     project_id        BIGINT,
#     class_id          BIGINT,
#     user_id           BIGINT       NOT NULL,
#     eks_id            BIGINT       NOT NULL,
#     post_title        VARCHAR(255) NOT NULL,
#     post_descr        VARCHAR(255) NOT NULL,
#     post_feedback_req VARCHAR(255) NOT NULL,
#     proj_version      INT          NOT NULL,
#     post_date         DATETIME     NOT NULL,
#     FOREIGN KEY (project_id) REFERENCES projects (projects_id),
#     FOREIGN KEY (class_id) REFERENCES classes (class_id),
#     FOREIGN KEY (user_id) REFERENCES users (id),
#     FOREIGN KEY (eks_id) REFERENCES knowledge_and_skills (eks_id)
) ENGINE InnoDB
  CHAR SET UTF8MB4;
