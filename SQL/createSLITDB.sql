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
  `email` varchar(50) NOT NULL,
  `tlf` int(8) Default null,
  primary key(`id`),
  Constraint `FK_Foreleser_Bruker` Foreign Key (`id`) references `bruker` (`id`)
);

CREATE TABLE if not exists`Kurs` (
`kursId` varchar(11) NOT NULL,
`kursNavn` varchar(50) NOT NULL,
`kursBilde`varchar(100),
`kursTekst` text,  
primary key(`kursId`)
);

CREATE TABLE if not exists `Modul` (
 `modulId` int(11) not null auto_increment,
 `kursId` varchar(11) not null,
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
  `kursId` varchar(11) Not Null,
  `studentId` int(11) Not null,
  primary key(`kursId`,`studentId`),
  Constraint `FK_TarKurs_Kurs` foreign key (`kursId`) references `kurs` (`kursId`),
  Constraint `FK_TarKurs_Student` foreign key (`studentId`) references `Student` (id) 
);

CREATE TABLE `ForeleserKurs` (
  `kursId` varchar(11) not null,
  `foreleserId` int(11) not null,
  primary key(`kursId`,`foreleserId`),
  Constraint `FK_ForeleserKurs_Kurs` foreign key (`kursId`) references `kurs` (`kursId`),
  Constraint `FK_ForeleserKurs_Student` foreign key (`foreleserId`) references `foreleser` (id) 
);

CREATE TABLE if not exists `Gruppe`(
    `gruppe_id` INT(11) NOT NULL auto_increment,
    `gruppenavn` VARCHAR(20) NOT NULL,
    `gruppeSkaper` INT(11) NOT NULL,
    PRIMARY KEY (`gruppe_id`),
    Constraint `FK_Gruppe_gruppeSkaperid` Foreign Key (`gruppeSkaper`) references `bruker` (`id`)
);

CREATE TABLE if not exists `Gruppetilbruker`(
    `id` INT(11),
    `gruppe_id` INT(11),
    PRIMARY KEY (`id`,`gruppe_id`),
      Constraint `FK_Gruppetilbruker_Bruker` Foreign Key (`id`) references `bruker` (`id`),
      Constraint `FK_Gruppetilbruker_Gruppe` Foreign Key (`gruppe_id`) references `gruppe` (`gruppe_id`)
);

CREATE TABLE if not exists `Gruppetilkurs`(
	`kursId` varchar(11) NOT NULL,
    `gruppe_id` INT(11) NOT NULL,
    PRIMARY KEY (`kursId`,`gruppe_id`),
      Constraint `FK_Gruppetilkurs_Kurs` Foreign Key (`kursId`) references `kurs` (`kursId`),
      Constraint `FK_Gruppetilkurs_Gruppe` Foreign Key (`gruppe_id`) references `gruppe` (`gruppe_id`)
);