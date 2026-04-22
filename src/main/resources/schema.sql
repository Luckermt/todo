CREATE TABLE IF NOT EXISTS todos (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    completed BOOLEAN DEFAULT FALSE,
    priority VARCHAR(10) CHECK (priority IN ('HIGH', 'MEDIUM', 'LOW')),
    deadline TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_todos_priority_deadline ON todos (priority, deadline);