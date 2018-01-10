-- Kategorie
INSERT INTO kategorie (nazwa, nadkategoria) VALUES ('Elektronarzedzia', NULL);
INSERT INTO kategorie (nazwa, nadkategoria) VALUES ('Wiertarki', 'Elektronarzedzia');
INSERT INTO kategorie (nazwa, nadkategoria) VALUES ('Wiertarki akumulatorowe', 'Wiertarki');
INSERT INTO kategorie (nazwa, nadkategoria) VALUES ('Wiertarki przewodowe', 'Wiertarki');
INSERT INTO kategorie (nazwa, nadkategoria) VALUES ('Elektronika', NULL);
INSERT INTO kategorie (nazwa, nadkategoria) VALUES ('RTV i AGD', 'Elektronika');
INSERT INTO kategorie (nazwa, nadkategoria) VALUES ('Telewizory', 'RTV i AGD');
INSERT INTO kategorie (nazwa, nadkategoria) VALUES ('Kuchenki mikrofalowe', 'RTV i AGD');

-- Towary
INSERT INTO towary (id, kod, opis, stan_dyspozycyjny, nazwa, cena_detaliczna, jednostka, waga, cena_hurtowa, prog_hurtowy, nazwa_kategori) VALUES (1, 'BOCH45398', 'Udarowa 600W, moment obrotowy 10,8 Nm, gwarancja 3 lata', 200, 'Wiertarka udarowa BOCH 5600', 319.00, 'szt', 0.91, 280.00, 20, 'Wiertarki przewodowe');
INSERT INTO towary (id, kod, opis, stan_dyspozycyjny, nazwa, cena_detaliczna, jednostka, waga, cena_hurtowa, prog_hurtowy, nazwa_kategori) VALUES (2, 'MKTIA98425', 'Udar, obroty prawo-lewo, walizka transportowa w zestawie', 50, 'Wiertarka Makika HK324', 352.99, 'szt', 2.0, 341.89, 22, 'Wiertarki przewodowe');
INSERT INTO towary (id, kod, opis, stan_dyspozycyjny, nazwa, cena_detaliczna, jednostka, waga, cena_hurtowa, prog_hurtowy, nazwa_kategori) VALUES (3, 'SMSNG00658', 'LED 52", 3x HDMI', 120, 'Telewizor SAMSUNG 52"', 1199.25, 'szt', 8.2, 1000.00, 10, 'Telewizory');
INSERT INTO towary (id, kod, opis, stan_dyspozycyjny, nazwa, cena_detaliczna, jednostka, waga, cena_hurtowa, prog_hurtowy, nazwa_kategori) VALUES (4, 'AMMCA89452', 'Moc 600W, grill elektryczny, funkcja QuickDefrost', 15, 'Kuchenka Mikrofalowa Amica K43', 360.47, 'szt', 3.2, 350.00, 11, 'Kuchenki mikrofalowe');

-- Klienci
INSERT INTO klienci (id, typ_klienta, email, numer, telefon) VALUES (1, 'I', 'jan.kowalski@gmail.com', '0000:0000:0000:0001', '503698145');
INSERT INTO klienci (id, typ_klienta, email, numer, telefon) VALUES (2, 'I', NULL, '0000:0000:0000:0002', '604259753');
INSERT INTO klienci (id, typ_klienta, email, numer, telefon) VALUES (3, 'P', NULL, '0000:0000:0000:0003', '(+47)8364495');
INSERT INTO klienci (id, typ_klienta, email, numer, telefon) VALUES (4, 'P', 'poltex@poltex.pl', '0000:0000:0000:0004', '(+49)3651598');

-- Konta Klientow
INSERT INTO kontaklientow (id, typ_klienta, email, login, haslo, status, id_klienta) VALUES (1, 'I', 'janek42@gmail.com', 'janek42', '65B58A8040C6978D82D5100BE261FDB5BCA5D804D96D9A842ADE4CEE99378CBD963941C485D8C9297621FFEE316C89537C9B73A0ED31C0FE0D9AEB849A5BC2AC', 'aktywne', 1);
INSERT INTO kontaklientow (id, typ_klienta, email, login, haslo, status, id_klienta) VALUES (2, 'I', 'marcin@wp.pl', 'magicM', 'BEA5B7534B5DDA222EBCBC44F42A7419BBB3B29D47CFCAC0C8BF4029795F54D0826659D9496794979572DC938A6FC61D1B88D727CAA94FF23CA9381932BA4742', 'aktywne', 2);
INSERT INTO kontaklientow (id, typ_klienta, email, login, haslo, status, id_klienta) VALUES (3, 'P', 'endopol@endopol.pl', 'endopol', 'D283014BF191C31D8AF8AD5E40C0665CB357C8C549C936E934AFA60227B89D69F1F1408BAEB0E6C0CD481D815E7D003AE3A09528AE0925F8EF9F358E09D167FD', 'aktywne', 3);
INSERT INTO kontaklientow (id, typ_klienta, email, login, haslo, status, id_klienta) VALUES (4, 'P', 'poltex@poltex.pl', 'poltex', 'D283014BF191C31D8AF8AD5E40C0665CB357C8C549C936E934AFA60227B89D69F1F1408BAEB0E6C0CD481D815E7D003AE3A09528AE0925F8EF9F358E09D167FD', 'aktywne', 4);

-- Klienci Indywidualni
INSERT INTO klienciindywidualni (imie, nazwisko, id_klienta) VALUES ('Jan', 'Nowak', 1);

-- Adresy
INSERT INTO Adresy (id, nr_budynku, miasto, nr_lokalu, kod_pocztowy, ulica) VALUES ('270C928989942094C4757F356D530C0D2A56E4F722E47EC0E35D7B50A553F9C8F3E0BA1F6E2E782BF2B25B8F6DE1A0CFC0B02840678B28A2B3FB841EBA4F0037', 34, 'Wroclaw', 2, '50-438', 'Lipowa');

-- Zamowienia
INSERT INTO zamowienia (id, data_odbioru, reklamacyjne, data_skompletowania, koszt_dostawy, czas_dostawy, faktura, numer, odbior_osobisty, czas_odroczenia, czas_realizacji, data_sprzedazy, status, data_zlozenia, id_klienta, adres_dostawy, adres_nabywcy) VALUES (1, '2017-12-02', 0, '2017-12-01', 0, NULL, 0, '0001/30-11-2017', 1, 0, NULL, '2017-12-02', 'realized', '2017-11-30', 1, NULL, NULL);
INSERT INTO zamowienia (id, data_odbioru, reklamacyjne, data_skompletowania, koszt_dostawy, czas_dostawy, faktura, numer, odbior_osobisty, czas_odroczenia, czas_realizacji, data_sprzedazy, status, data_zlozenia, id_klienta, adres_dostawy, adres_nabywcy) VALUES (2, '2017-12-11', 0, '2017-12-08', 50.00, 3, 1, '0001/06-12-2017', 0, 0, 5, '2017-12-11', 'realized', '2017-12-06', 1, '270C928989942094C4757F356D530C0D2A56E4F722E47EC0E35D7B50A553F9C8F3E0BA1F6E2E782BF2B25B8F6DE1A0CFC0B02840678B28A2B3FB841EBA4F0037', '270C928989942094C4757F356D530C0D2A56E4F722E47EC0E35D7B50A553F9C8F3E0BA1F6E2E782BF2B25B8F6DE1A0CFC0B02840678B28A2B3FB841EBA4F0037');

-- Pozycje Zamowien
INSERT INTO pozycjezamowien (numer, cena, ilosc, id_towaru, id_zamowienia) VALUES (1, 319.00, 5, 1, 1);
INSERT INTO pozycjezamowien (numer, cena, ilosc, id_towaru, id_zamowienia) VALUES (2, 352.99, 2, 2, 1);
INSERT INTO pozycjezamowien (numer, cena, ilosc, id_towaru, id_zamowienia) VALUES (3, 1119.25, 3, 3, 1);

INSERT INTO pozycjezamowien (numer, cena, ilosc, id_towaru, id_zamowienia) VALUES (1, 1119.25, 2, 3, 2);
INSERT INTO pozycjezamowien (numer, cena, ilosc, id_towaru, id_zamowienia) VALUES (2, 360.47, 5, 4, 2);