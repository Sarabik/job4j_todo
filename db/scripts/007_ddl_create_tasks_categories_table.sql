CREATE TABLE if NOT EXISTS tasks_categories (
    id          serial PRIMARY KEY,
    task_id     INT NOT NULL REFERENCES tasks(id),
    category_id    INT NOT NULL REFERENCES categories(id),
    UNIQUE (task_id, category_id)
);