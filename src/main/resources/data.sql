-- Voeg de gebruiker toe
INSERT INTO members (id, email, password) VALUES (1, 'member@example.com', '$2a$10$HtSvAyCN0d4yZMSZ4bEFwuteGxs5RDrWNV.s/Ls2fFB9EPnMYwRei');
INSERT INTO members (id, email, password) VALUES (2, 'info@filmland-assessment.nl', '$2a$10$dzmLYXMupzAk/gPmLigyXevJaPbtxlwbwID0NeWKOt3oYy3X8HCd2');
INSERT INTO members (id, email, password) VALUES (3, 'client@filmland-assessment.nl', '$2a$10$U9d1cmjJxcWrhxwTbp3lY.FU7kPcogBfpS09za1Dpsh1OkIYRGuFy');


-- Voeg beschikbare dummy categorieën toe
INSERT INTO categories (id, name, content_limit, price) VALUES (1, 'Dutch Films', 10, 4.0);
INSERT INTO categories (id, name, content_limit, price) VALUES (2, 'Dutch Series', 20, 6.0);
INSERT INTO categories (id, name, content_limit, price) VALUES (3, 'International Films', 30, 8.0);

-- Voeg een abonnement toe met expliciete user_id en category_id
INSERT INTO subscriptions (id, remaining_content, start_date, category_id, member_id)
VALUES (1, 5, '2018-01-01', 3, 1);

-- Voeg een extra abonnement toe voor testen
INSERT INTO subscriptions (id, remaining_content, start_date, category_id, member_id)
VALUES (2, 10, '2025-02-13', 1, 2);

-- Voeg een gedeeld abonnement toe
INSERT INTO subscriptions (id, remaining_content, start_date, shared_with_email, shared_start_date, category_id, member_id)
VALUES (3, 5, '2025-02-13', 'client@filmland-assessment.nl', '2025-02-14', 1, 2);
