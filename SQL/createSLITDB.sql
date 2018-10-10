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

CREATE TABLE if not exists `modulListe` (
  `modul_Id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int(11) NOT NULL,
  `modul_Navn` varchar(20) NOT NULL,
  `modul_Nummer` int (30) NOT NULL,
  primary key(`modul_Id`),
  Constraint `FK_Foreleser_ModulListe` Foreign Key (`id`) references `Foreleser`(`id`)
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
 
 CREATE TABLE if not exists `Innlevering` (
  `innlev_Id` int(11) NOT NULL AUTO_INCREMENT,
  `modul_Id` int (11) NOT NULL,
  `modul_Nummer` int (11) NOT NULL,
  `id` int (11) NOT NULL,
  `innlev_Kommentar` varchar (250) NOT NULL,
  `innlev_Poeng` int NOT NULL,
  primary key(`innlev_Id`),
  Constraint `FK_ModulListe_Innlevering` Foreign Key (`modul_Id`) references `ModulListe` (`modul_Id`),
  Constraint `FK_Bruker_Innlevering` Foreign Key (`id`) references `Bruker` (`id`)
  );
  
  CREATE TABLE if not exists `Kommentarer` (
  `kom_Id` int(11) NOT NULL AUTO_INCREMENT,
  `id` int (11) NOT NULL,
  `innlev_Id` int(11) NOT NULL,
  `kom_kommentar` varchar (250) NOT NULL,
  primary key(`kom_Id`),
  Constraint `FK_Kommentarer_Bruker` Foreign Key (`id`) references `Bruker` (`id`),
  Constraint `FK_Kommentarer_Innlev` Foreign Key (`innlev_Id`) references `Innlevering` (`innlev_Id`)
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
