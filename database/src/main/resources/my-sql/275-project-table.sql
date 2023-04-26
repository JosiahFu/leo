CREATE TABLE project
(
    id                          INT PRIMARY KEY AUTO_INCREMENT,
    creation_time               DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,

    name                        VARCHAR(255) NOT NULL,
    short_descr                 TEXT,
    short_descr_quill           TEXT,
    long_descr                  TEXT,
    long_descr_quill            TEXT,
    state                       ENUM('UNDECIDED', 'THUMBS_UP', 'THUMBS_DOWN'),
    needs_review                TINYINT,  -- Boolean 0 = false.

    waiting_for_results_timeout DATETIME,
    ikigai_input_id             INT,
    CONSTRAINT project__ikigai_input_id
        FOREIGN KEY (ikigai_input_id)
            REFERENCES ikigai_input (id)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE InnoDB
  CHAR SET UTF8MB4;
