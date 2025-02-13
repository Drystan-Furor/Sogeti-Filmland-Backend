-- Voeg de gebruiker toe
INSERT INTO members (id, email, password) VALUES (1, 'member@example.com', '$2a$10$HtSvAyCN0d4yZMSZ4bEFwuteGxs5RDrWNV.s/Ls2fFB9EPnMYwRei');
INSERT INTO members (id, email, password) VALUES (2, 'info@filmland-assessment.nl', '$2a$10$dzmLYXMupzAk/gPmLigyXevJaPbtxlwbwID0NeWKOt3oYy3X8HCd2');


-- Voeg beschikbare dummy categorieën toe
INSERT INTO categories (id, name, content_limit, price) VALUES (1, 'Dutch Films', 10, 4.0);
INSERT INTO categories (id, name, content_limit, price) VALUES (2, 'Dutch Series', 20, 6.0);
INSERT INTO categories (id, name, content_limit, price) VALUES (3, 'International Films', 30, 8.0);

-- Voeg een abonnement toe met expliciete user_id en category_id
INSERT INTO subscriptions (id, remaining_content, start_date, category_id, member_id)
VALUES (1, 5, '2018-01-01', 3, 1);
