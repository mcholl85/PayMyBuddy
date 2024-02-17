INSERT INTO
    users (email, password, lastname, firstname, balance)
VALUES
    (
        'user1@example.com',
        'password1',
        'Nom1',
        'Prénom1',
        100.00
    ),
    (
        'user2@example.com',
        'password2',
        'Nom2',
        'Prénom2',
        150.00
    ),
    (
        'user3@example.com',
        'password3',
        'Nom3',
        'Prénom3',
        200.00
    ),
    (
        'user4@example.com',
        'password4',
        'Nom4',
        'Prénom4',
        250.00
    ),
    (
        'user5@example.com',
        'password5',
        'Nom5',
        'Prénom5',
        300.00
    );

INSERT INTO
    transactions (
        amount,
        fee,
        description,
        sender_id,
        recipient_id
    )
VALUES
    (50.00, 0.50, 'Transaction 1', 1, 2),
    (100.00, 1.00, 'Transaction 2', 2, 3),
    (150.00, 1.50, 'Transaction 3', 3, 4),
    (200.00, 2.00, 'Transaction 4', 4, 5),
    (250.00, 2.50, 'Transaction 5', 5, 1);

INSERT INTO
    connexions (user1_id, user2_id)
VALUES
    (1, 2),
    (2, 3),
    (3, 4),
    (4, 5),
    (5, 1);