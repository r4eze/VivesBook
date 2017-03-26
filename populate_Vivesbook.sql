insert into account(naam, voornaam, login, paswoord, emailadres, geslacht) values('Matthias','Oplinus','Mato','Laura0401','Mato@domein.be','M');
insert into account(naam, voornaam, login, paswoord, emailadres, geslacht) values('Laura', 'Raes', 'Lara', 'Mat2608','laura@hotmail.com','V');
insert into account(naam, voornaam, login, paswoord, emailadres, geslacht) values('Philip', 'Lesaffer', 'Phila', 'Koda38', 'ann@telenet.be', 'M');
insert into vriendschap values('Mato', 'Lara');
insert into vriendschap values('Lara', 'Mato');
insert into vriendschap values('Phila', 'Lara');
insert into vriendschap values('Lara', 'Phila');
insert into post(tekst, eigenaar) values('Hallo dit is een post vanlikes Mato', 'Mato');
insert into post(tekst, eigenaar) values('Dit is dan weer een post van Lara', 'Lara');
insert into post(tekst, eigenaar) values('En deze is dan weer van Phila', 'Phila');
insert into post(tekst, eigenaar) values('Dit is de tweede post van Mato', 'Mato');
insert into likes values('Mato',2,'geweldig');
insert into likes values('Lara', 1, 'leuk');
insert into likes values('Phila', 2, 'boos');

