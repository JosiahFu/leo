CREATE TABLE portfolio_post
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    name              VARCHAR(255) NOT NULL,
    short_descr_quill BLOB         NOT NULL,
    long_descr_quill  LONGBLOB     NOT NULL,

    order_index       INT          NOT NULL,

    portfolio_id      INT          NOT NULL,
    CONSTRAINT portfolio_post_portfolio_id
        FOREIGN KEY (portfolio_id)
            REFERENCES portfolio (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id   INT          NOT NULL,
    CONSTRAINT portfolio_post_project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
