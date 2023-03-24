CREATE TABLE knowledge_and_skills 
(
  eks_id            BIGINT         PRIMARY KEY,
  project_id        BIGINT,
  class_id          BIGINT,
  user_id           BIGINT,        NOT NULL,
  eks_title         VARCHAR(255)   NOT NULL,
  eks_descr         VARCHAR(255)   NOT NULL,
  mastery_1         VARCHAR(255)   NOT NULL,
  mastery_2         VARCHAR(255)   NOT NULL,
  mastery_3         VARCHAR(255)   NOT NULL,
  mastery_4         VARCHAR(255)   NOT NULL,
  FOREIGN KEY (project_id) REFERENCES projects(id),
  FOREIGN KEY (class_id) REFERENCES classes(id)
) CHAR SET `UTF8MB4`;
