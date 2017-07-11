DROP TABLE IF EXISTS city;

CREATE TABLE city (
  id      INT(6) PRIMARY KEY AUTO_INCREMENT,
  NAME    VARCHAR(64),
  state   VARCHAR(32),
  country VARCHAR(32)
);

INSERT INTO city (NAME, state, country) VALUES ('San Francisco', 'CA', 'US');