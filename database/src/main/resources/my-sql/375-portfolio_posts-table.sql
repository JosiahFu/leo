CREATE TABLE portfolio_posts
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    title             VARCHAR(255) NOT NULL,
    short_descr_quill BLOB         NOT NULL,
    long_descr_quill  LONGBLOB     NOT NULL,

    order_index       INT          NOT NULL,

    portfolio_id      INT          NOT NULL,
    INDEX (portfolio_id),
    CONSTRAINT portfolios_project_posts_portfolio_id
        FOREIGN KEY (portfolio_id)
            REFERENCES portfolios (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id   INT          NOT NULL,
    INDEX (project_post_id),
    CONSTRAINT portfolios_project_posts_project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_posts (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
