DELETE FROM books;
DELETE FROM users;

INSERT INTO books(id, title, author, amount)
	VALUES
 		('42d3f123-dd2f-4a10-a182-6506edd9d355', 'Clean Code', 'Robert Martin', 1),
    ('2decc0bd-9730-4145-b18e-94029dfb961f', 'Effective Java', 'Bloch Joshua', 2);

INSERT INTO users(id, name, membership_date)
	VALUES('f0d9bdfc-38e7-4a34-b07f-8216574efbb5', 'John Doe', '2024-08-13');

INSERT INTO users_books(id, user_id, book_id)
	VALUES
  	('a362bfd4-6726-424e-917f-ebc56dc6e4b4', 'f0d9bdfc-38e7-4a34-b07f-8216574efbb5',
    '2decc0bd-9730-4145-b18e-94029dfb961f')