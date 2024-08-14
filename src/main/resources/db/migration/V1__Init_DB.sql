CREATE TABLE users(
	id VARCHAR(36),
	name VARCHAR(256) NOT NULL,
	membership_date DATE,
	PRIMARY KEY(id)
);

CREATE index users_fl_idx ON users (name);

CREATE TABLE books(
	id VARCHAR(36),
	title VARCHAR(256) NOT NULL,
	author VARCHAR(256) NOT NULL,
	amount INTEGER,
	PRIMARY KEY(id)
);

CREATE index books_ta_idx ON books(author, title);

CREATE TABLE users_books (
	id VARCHAR(36),
	user_id VARCHAR(36) NOT NULL,
	book_id VARCHAR(36) NOT NULL,
	PRIMARY KEY(id),
	CONSTRAINT fk_users_books_users FOREIGN KEY(user_id) REFERENCES users(id),
	CONSTRAINT fk_users_books_books FOREIGN key(book_id) REFERENCES books(id)
);
