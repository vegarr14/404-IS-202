drop schema slitdb;

Create schema if not exists SLITDB;
USE SLITDB;

CREATE TABLE if not exists`Bruker` (
  `id` int(11) NOT null AUTO_INCREMENT,
  `brukerNavn` varchar(20) NOT NULL,
  `passord` varbinary(200) NOT NULL,
  primary key(`id`)
);

CREATE TABLE if not exists`Student` (
  `id` int(11) NOT null,
  `forNavn` varchar(20) NOT NULL,
  `etterNavn` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL,
  `tlf` int(8) Default null,
  primary key(`id`),
  Constraint `FK_Student_Bruker` Foreign Key (`id`) references `bruker` (`id`)
);

CREATE TABLE if not exists`Foreleser` (
  `id` int(11) NOT null,
  `forNavn` varchar(20) NOT NULL,
  `etterNavn` varchar(20) NOT NULL,
  `email` varchar(20) NOT NULL,
  `tlf` int(8) Default null,
  primary key(`id`),
  Constraint `FK_Foreleser_Bruker` Foreign Key (`id`) references `bruker` (`id`)
);

CREATE TABLE if not exists `Gruppe`(
    `gruppe_id` INT(11) NOT NULL auto_increment,
    `gruppenavn` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`gruppe_id`)
);
CREATE TABLE if not exists `Gruppetilbruker`(
    `id` INT(11),
    `gruppe_id` INT(11)
);

INSERT INTO Gruppe (gruppenavn) Values ("Gruppe404");
INSERT INTO Gruppe (gruppenavn) Values ("Gruppe505");
INSERT INTO Gruppe (gruppenavn) Values ("Gruppe606");
INSERT INTO Gruppe (gruppenavn) Values ("Gruppe707");

INSERT INTO gruppetilbruker (id,gruppe_id) VALUES
(7, 1),
(3, 1),	
(2, 1),
(1, 1);	

CREATE TABLE if not exists `modulListe` (
  `modul_Id` int(11) NOT NULL AUTO_INCREMENT,
  `modul_Navn` varchar(20) NOT NULL,
  `modul_Nummer int (30) NOT NULL,
  primary key(`modul_Id`)
  );


INSERT into bruker (brukernavn, passord) Values ('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto'));

INSERT into Student values('1','Vegar','Ryen','emailxd','12345678'),
('2','Sondre','Hammersbøen','emailcx','87654321'),
('3','Erlend','Thorsen','mailmail','12344321');

INSERT into modulListe (`modul_Navn`, `modul_Nummer`) 
values ('Modul', 1), 
('Modul', 2), 
('Modul', 3), 
('Modul', 4), 
('Modul', 5);
                     
select * from modulListe;
