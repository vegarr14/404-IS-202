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

CREATE TABLE if not exists `modulListe` (
  `modul_Id` int(11) NOT null,
  `modul_Navn` varchar(20) NOT NULL,
  primary key(`modul_Id`)
  );

CREATE TABLE if not exists`Kurs` (
`id` int(6) NOT NULL auto_increment,
`kursid` varchar(6) NOT NULL,
`kursnavn` varchar(50) NOT NULL,
primary key(`id`)
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

INSERT into modulListe (`modul_Id`, `modul_Navn`) 
values (1, 'Modul'), 
(2, 'Modul'), 
(3, 'Modul'), 
(4, 'Modul'), 
(5, 'Modul');
                     
select * from bruker;
