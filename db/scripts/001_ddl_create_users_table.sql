CREATE TABLE if NOT EXISTS users (
    id          serial  PRIMARY KEY,
    name        VARCHAR,
    login       VARCHAR UNIQUE NOT NULL,
    password    VARCHAR NOT NULL
);