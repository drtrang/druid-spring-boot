DROP TABLE IF EXISTS city;

CREATE TABLE city (
  id      INT PRIMARY KEY AUTO_INCREMENT,
  NAME    VARCHAR(64),
  state   VARCHAR(16),
  country VARCHAR(16)
);

INSERT INTO city (NAME, state, country) VALUES ('Beijing', 'BJ', 'CN');
INSERT INTO city (NAME, state, country) VALUES ('Shanghai', 'SH', 'CN');
INSERT INTO city (NAME, state, country) VALUES ('Guangzhou', 'GZ', 'CN');
INSERT INTO city (NAME, state, country) VALUES ('Shenzhen', 'SZ', 'CN');
INSERT INTO city (NAME, state, country) VALUES ('San Francisco', 'CA', 'US');