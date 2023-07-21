CREATE TABLE if NOT EXISTS priorities (
   id SERIAL PRIMARY KEY,
   name TEXT UNIQUE NOT NULL,
   position int
);

