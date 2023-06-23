INSERT INTO documentprocessorapp.client (clientName, mail, password)
VALUES
    ('John Doe', 'johndoe@example.com', 'password123'),
    ('Jane Smith', 'janesmith@example.com', 'password456'),
    ('Mike Johnson', 'mikejohnson@example.com', 'password789');
INSERT INTO documentprocessorapp.filetype (typeName)
VALUES
    ('pdf'),
    ('rtf'),
    ('xls'),
    ('xlsx');
INSERT INTO documentprocessorapp.file (name, path, typeId, clientId, lastModfied)
VALUES
    ('Document1.pdf', '/path/to/document1.pdf', 1, 1, '2023-05-25 10:30:00'),
    ('Document2.doc', '/path/to/document2.doc', 2, 1, '2023-05-26 14:45:00'),
    ('Document3.txt', '/path/to/document3.txt', 3, 2, '2023-05-24 09:15:00'),
    ('Document4.pdf', '/path/to/document4.pdf', 1, 3, '2023-05-25 16:20:00'),
    ('Document5.doc', '/path/to/document5.doc', 2, 3, '2023-05-26 11:00:00');
