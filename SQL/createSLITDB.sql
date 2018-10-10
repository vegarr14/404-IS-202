drop schema slitdb;

Create schema if not exists SLITDB;
USE SLITDB;

drop table if exists Kommentarer;
drop table if exists Innlevering;
drop table if exists ModulListe;
drop table if exists Modul;
drop table if exists Kurs;
drop table if exists Foreleser;
drop table if exists Student;
drop table if exists Bruker;

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

CREATE TABLE if not exists`Kurs` (
`kursId` int(11) NOT NULL AUTO_INCREMENT,
`kursNavn` varchar(50) NOT NULL,
primary key(`kursId`)
);

CREATE TABLE if not exists `Modul` (
 `modulId` int(11) not null auto_increment,
 `kursId` int(11) not null,
 `foreleserId` int(11) not null,
 `modulNummer` int(11) not null,
 `oppgaveTekst` text not null,
 primary key(`modulId`),
 constraint `FK_Modul_Kurs` foreign key (`kursId`) references `Kurs` (`kursId`),
 constraint `FK_Modul_Forelser` foreign key (`foreleserId`) references `Foreleser` (`id`)
 );
 
 CREATE TABLE if not exists `Innlevering` (
  `innlevId` int(11) NOT NULL AUTO_INCREMENT,
  `modulId` int (11) NOT NULL,
  `modulNummer` int (11) NOT NULL,
  `id` int (11) NOT NULL,
  `innlevKommentar` varchar (250) NOT NULL,
  `innlevPoeng` int NOT NULL,
  primary key(`innlevId`),
  Constraint `FK_ModulListe_Innlevering` Foreign Key (`modulId`) references `Modul` (`modulId`),
  Constraint `FK_Bruker_Innlevering` Foreign Key (`id`) references `Bruker` (`id`)
  );
  
  CREATE TABLE if not exists `Kommentarer` (
  `komId` int(11) NOT NULL AUTO_INCREMENT,
  `id` int (11) NOT NULL,
  `innlevId` int(11) NOT NULL,
  `komKommentar` varchar (250) NOT NULL,
  primary key(`komId`),
  Constraint `FK_Kommentarer_Bruker` Foreign Key (`id`) references `Bruker` (`id`),
  Constraint `FK_Kommentarer_Innlev` Foreign Key (`innlevId`) references `Innlevering` (`innlevId`)
  );

CREATE TABLE `TarKurs` (
  `kursId` int(6) Not Null,
  `studentId` int(11) Not null,
  primary key(`kursId`,`studentId`),
  Constraint `FK_TarKurs_Kurs` foreign key (`kursId`) references `kurs` (`kursId`),
  Constraint `FK_TarKurs_Student` foreign key (`studentId`) references `Student` (id) 
);

CREATE TABLE `ForeleserKurs` (
  `kursId` int(6) not null,
  `foreleserId` int(11) not null,
  Constraint `FK_ForeleserKurs_Kurs` foreign key (`kursId`) references `kurs` (`kursId`),
  Constraint `FK_ForeleserKurs_Student` foreign key (`foreleserId`) references `foreleser` (id) 
);
