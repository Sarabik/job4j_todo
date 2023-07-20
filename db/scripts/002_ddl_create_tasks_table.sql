CREATE TABLE if NOT EXISTS tasks (
   id SERIAL PRIMARY KEY,
   title VARCHAR NOT NULL,
   description TEXT,
   created TIMESTAMP,
   done BOOLEAN,
   users_id INT NOT NULL references users(id) ON DELETE CASCADE
);