INSERT INTO users (email, password, lastname, firstname, balance)
VALUES ('emilie.dubois@gmail.com', '$2y$10$DRc86ecD96TuHvYH3uOTOeV.NFeL4j7wwZ.qFTMRJThZxWauwysF6', 'Dubois', 'Emilie',
        1500.50),
       ('lucas.martin@hotmail.fr', '$2y$10$DRc86ecD96TuHvYH3uOTOeV.NFeL4j7wwZ.qFTMRJThZxWauwysF6', 'Martin', 'Lucas',
        780.95),
       ('sophie.bernard@outlook.fr', '$2y$10$DRc86ecD96TuHvYH3uOTOeV.NFeL4j7wwZ.qFTMRJThZxWauwysF6', 'Bernard',
        'Sophie', 1220.00),
       ('hugo.petit@yahoo.fr', '$2y$10$DRc86ecD96TuHvYH3uOTOeV.NFeL4j7wwZ.qFTMRJThZxWauwysF6', 'Petit', 'Hugo', 320.45),
       ('chloe.roux@icloud.com', '$2y$10$DRc86ecD96TuHvYH3uOTOeV.NFeL4j7wwZ.qFTMRJThZxWauwysF6', 'Roux', 'Chloe',
        540.30);

INSERT INTO transactions (amount, fee, description, sender_id, recipient_id)
VALUES (100.00, 2.00, 'Remboursement diner', 1, 2),
       (25.50, 0.50, 'Cadeau anniversaire', 3, 4),
       (150.75, 3.00, 'Vente vélo', 2, 5),
       (48.25, 1.00, 'Partage facture internet', 4, 1),
       (200.00, 4.00, 'Prêt urgent', 5, 3),
       (15.00, 0.30, 'Remboursement café', 1, 3),
       (110.00, 2.20, 'Achat groupé jeux', 4, 2),
       (70.50, 1.40, 'Paiement cours de yoga', 3, 5),
       (30.00, 0.60, 'Dédommagement retard', 2, 1),
       (45.75, 0.90, 'Vente livre occasion', 5, 4),
       (130.00, 2.60, 'Contribution voyage', 1, 5),
       (55.25, 1.10, 'Remboursement billet concert', 3, 2),
       (85.00, 1.70, 'Achat cadeau commun', 4, 3),
       (60.00, 1.20, 'Split facture restaurant', 5, 1),
       (90.00, 1.80, 'Contribution cadeau mariage', 2, 4),
       (20.00, 0.40, 'Remboursement essence', 3, 1),
       (75.00, 1.50, 'Participation achat groupé', 4, 5),
       (65.00, 1.30, 'Remboursement prêt livre', 1, 4),
       (40.00, 0.80, 'Cotisation club sportif', 2, 3),
       (50.00, 1.00, 'Paiement leçon de musique', 5, 2);


INSERT INTO connexions (user1_id, user2_id)
VALUES (1, 2),
       (3, 4),
       (2, 5),
       (5, 3),
       (1, 3),
       (4, 2),
       (5, 4),
       (1, 5),
       (2, 4),
       (4, 5),
       (1, 4),
       (2, 3);
