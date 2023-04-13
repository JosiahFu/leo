CREATE TABLE portfolio_post
(
    id                INT PRIMARY KEY AUTO_INCREMENT,

    title             VARCHAR(255) NOT NULL,
    short_desc            VARCHAR(2048)  NOT NULL,
    short_descr_quill_zip BLOB,
    long_desc             VARCHAR(16384) NOT NULL,
    long_descr_quill_zip  BLOB,

    order_index       INT          NOT NULL,

    portfolio_id      INT          NOT NULL,
    INDEX (portfolio_id),
    CONSTRAINT portfolio_post_portfolio_id
        FOREIGN KEY (portfolio_id)
            REFERENCES portfolio (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT,

    project_post_id   INT          NOT NULL,
    INDEX (project_post_id),
    CONSTRAINT portfolio_post_project_post_id
        FOREIGN KEY (project_post_id)
            REFERENCES project_post (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
