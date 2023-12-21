CREATE SCHEMA IF NOT EXISTS school;

CREATE TABLE IF NOT EXISTS school.class (
                                            id SERIAL PRIMARY KEY,
                                            name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS school.subject (
                                              id SERIAL PRIMARY KEY,
                                              name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS school.student (
                                              id SERIAL PRIMARY KEY,
                                              name VARCHAR(255) NOT NULL,
                                              class_id INT,
                                              FOREIGN KEY (class_id) REFERENCES school.class(id)
);

CREATE TABLE IF NOT EXISTS school.mark (
                                           id SERIAL PRIMARY KEY,
                                           value INT NOT NULL,
                                           subject_id INT,
                                           student_id INT,
                                           date DATE NOT NULL,
                                           FOREIGN KEY (subject_id) REFERENCES school.subject(id),
                                           FOREIGN KEY (student_id) REFERENCES school.student(id)
);

CREATE TABLE IF NOT EXISTS school.class_subject (
                                                    class_id INT,
                                                    subject_id INT,
                                                    PRIMARY KEY (class_id, subject_id),
                                                    FOREIGN KEY (class_id) REFERENCES school.class(id),
                                                    FOREIGN KEY (subject_id) REFERENCES school.subject(id)
);
