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
  `modul_Id` int(11) NOT NULL AUTO_INCREMENT,
  `modul_Navn` varchar(20) NOT NULL,
  `modul_Nummer int (30) NOT NULL,
  primary key(`modul_Id`)
  );

CREATE TABLE if not exists`Kurs` (
`id` int(6) NOT NULL auto_increment,
`kursid` varchar(6) NOT NULL,
`kursnavn` varchar(50) NOT NULL,
primary key(`id`)
);

CREATE TABLE if not exists `Modul` (
 `id` int(11) not null auto_increment,
 `kursId` int(6) not null,
 `foreleserId` int(11) not null,
 `modulNummer` int(11) not null,
 `tekst` text not null,
 primary key(`id`),
 constraint `FK_Modul_Kurs` foreign key (`kursId`) references `Kurs` (`id`),
 constraint `FK_Modul_Forelser` foreign key (`foreleserId`) references `Foreleser` (`id`)
 );

CREATE TABLE `TarKurs` (
  `kursId` int(6) Not Null,
  `studentId` int(11) Not null,
  primary key(`kursId`,`studentId`),
  Constraint `FK_TarKurs_Kurs` foreign key (`kursId`) references `kurs` (id),
  Constraint `FK_TarKurs_Student` foreign key (`studentId`) references `Student` (id) 
);

CREATE TABLE `ForeleserKurs` (
  `kursId` int(6) not null,
  `foreleserId` int(11) not null,
  Constraint `FK_ForeleserKurs_Kurs` foreign key (`kursId`) references `kurs` (id),
  Constraint `FK_ForeleserKurs_Student` foreign key (`foreleserId`) references `foreleser` (id) 
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
                     
INSERT into Foreleser values ('4','Hans','Mr.olav','yo@yahoo.com','99999999');

insert into Kurs values('1','IS-200','Systemanalyse og systemutvikling'),
('2','IS-201','Datamodellering og databasesystemer'),
('3','IS-202','Programmeringsprosjekt');

INSERT INTO Modul values ('1','1','4','1','You`re wondering who i am!'),
('2','1','4','2','machine or mannequin. With parts made in Japan, I am the modern man!');

insert into TarKurs values('1','1'),
('2','1'),
('3','2');

insert into ForeleserKurs values('1','4');

INSERT into modulListe (`modul_Navn`, `modul_Nummer`) 
values ('Modul', 1), 
('Modul', 2), 
('Modul', 3), 
('Modul', 4), 
('Modul', 5);
                     
/* select * from modulListe; */
