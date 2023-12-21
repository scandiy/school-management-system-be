CREATE TABLE school.roles (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(20)
);
INSERT INTO school.roles (name) VALUES ('ROLE_USER');
INSERT INTO school.roles (name) VALUES ('ROLE_MODERATOR');
INSERT INTO school.roles (name) VALUES ('ROLE_ADMIN');

CREATE TABLE school.users (
                       id SERIAL PRIMARY KEY,
                       username VARCHAR(20) NOT NULL,
                       email VARCHAR(50) NOT NULL,
                       password VARCHAR(120) NOT NULL,
                       UNIQUE (username),
                       UNIQUE (email)
);

CREATE TABLE school.user_roles (
                            user_id SERIAL,
                            role_id SERIAL,
                            FOREIGN KEY (user_id) REFERENCES school.users(id),
                            FOREIGN KEY (role_id) REFERENCES school.roles(id),
                            PRIMARY KEY (user_id, role_id)
);
