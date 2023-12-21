CREATE TABLE school.endofday (
                                 symbol VARCHAR(255) NOT NULL,
                                 date DATE NOT NULL,
                                 close FLOAT NOT NULL,
                                 PRIMARY KEY (symbol, date)
);
