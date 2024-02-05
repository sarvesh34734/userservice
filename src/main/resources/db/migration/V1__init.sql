CREATE TABLE session
(
    id      BINARY(16) NOT NULL,
    token   VARCHAR(255) NULL,
    status  SMALLINT NULL,
    user_id BINARY(16) NULL,
    CONSTRAINT pk_session PRIMARY KEY (id)
);

CREATE TABLE user
(
    id      BINARY(16) NOT NULL,
    name    VARCHAR(255) NULL,
    email   VARCHAR(255) NULL,
    encpass VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE session
    ADD CONSTRAINT FK_SESSION_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);