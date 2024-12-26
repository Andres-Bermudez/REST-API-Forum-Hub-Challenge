CREATE TABLE users (
    id_user BIGSERIAL PRIMARY KEY,

    user_name VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    user_password TEXT NOT NULL,
    active_status BOOLEAN DEFAULT TRUE
);

CREATE TABLE courses (
    id_course BIGSERIAL PRIMARY KEY,

    name VARCHAR(100) NOT NULL,
    category VARCHAR(100) NOT NULL
);

CREATE TABLE topics (
    id_topic BIGSERIAL PRIMARY KEY,

    title VARCHAR(100) NOT NULL,
    id_course BIGINT NOT NULL,
    id_user BIGINT NOT NULL,
    message TEXT NOT NULL,
    created_date TIMESTAMP NOT NULL,
    last_edited_at TIMESTAMP NOT NULL,
    active_status BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (id_course) REFERENCES courses(id_course) ON DELETE CASCADE,
    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE
);

CREATE TABLE answers (
    id_answer BIGSERIAL PRIMARY KEY,

    id_user BIGINT NOT NULL,
    id_topic BIGINT NOT NULL,
    message TEXT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    last_edited_at TIMESTAMP NOT NULL,
    active_status BOOLEAN DEFAULT TRUE,

    FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
    FOREIGN KEY (id_topic) REFERENCES topics(id_topic) ON DELETE CASCADE
);
