CREATE TABLE users (
    id_user SERIAL PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password TEXT NOT NULL,
    active_status BOOLEAN NOT NULL
);

CREATE TABLE courses (
    id_course SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(100) NOT NULL
);

CREATE TABLE topics (
    id_topic SERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL UNIQUE,
    course_id INT REFERENCES courses(id_course) ON DELETE CASCADE,
    user_id INT REFERENCES users(id_user) ON DELETE CASCADE,
    message TEXT NOT NULL UNIQUE,
    created_date TIMESTAMP NOT NULL
);

CREATE TABLE answers (
    id_answer SERIAL PRIMARY KEY,
    user_id INT REFERENCES users(id_user) ON DELETE CASCADE,
    topic_id INT REFERENCES topics(id_topic) ON DELETE CASCADE,
    message TEXT NOT NULL,
    created_date TIMESTAMP NOT NULL
);


