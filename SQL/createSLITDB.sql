drop schema slitdb;

Create schema if not exists SLITDB;
USE SLITDB;

SET GLOBAL max_allowed_packet=1073741824;

drop table if exists Kommentarer;
drop table if exists Innlevering;
drop table if exists ModulListe;
drop table if exists Modul;
drop table if exists Kurs;
drop table if exists Foreleser;
drop table if exists Student;
drop table if exists Bruker;
drop table if exists Gruppe;
drop table if exists Gruppetilbruker;
drop table if exists Gruppetilkurs;
drop table if exists notifikasjoner;


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

CREATE TABLE if not exists `Gruppe`(
    `gruppeId` INT(11) NOT NULL auto_increment,
    `gruppeNavn` VARCHAR(20) NOT NULL,
    `gruppeSkaperId` INT(11) NOT NULL,
    PRIMARY KEY (`gruppeId`),
    Constraint `FK_Gruppe_gruppedSkaperid` Foreign Key (`gruppeSkaperId`) references `bruker` (`id`)
);

CREATE TABLE if not exists `Gruppetilbruker`(
    `id` INT(11),
    `gruppeId` INT(11),
    PRIMARY KEY (`id`,`gruppeId`),
      Constraint `FK_Gruppetilbruker_Bruker` Foreign Key (`id`) references `bruker` (`id`),
      Constraint `FK_Gruppetilbruker_Gruppe` Foreign Key (`gruppeId`) references `gruppe` (`gruppeId`) ON DELETE CASCADE
);

CREATE TABLE if not exists `Gruppetilkurs`(
	`kursId` varchar(11) NOT NULL,
    `gruppeId` INT(11) NOT NULL,
    PRIMARY KEY (`kursId`,`gruppeId`),
      Constraint `FK_Gruppetilkurs_Kurs` Foreign Key (`kursId`) references `kurs` (`kursId`) ON DELETE CASCADE,
      Constraint `FK_Gruppetilkurs_Gruppe` Foreign Key (`gruppeId`) references `gruppe` (`gruppeId`) ON DELETE CASCADE
);

CREATE TABLE if not exists `Modul` (
 `modulId` int(11) not null auto_increment,
 `kursId` varchar(11) not null,
 `foreleserId` int(11) not null,
 `modulNummer` int(11) not null,
 `oppgaveTekst` text not null,
 `levereSomGruppe` boolean,
 `maxPoeng` int,
 `innleveringsFrist` timestamp default null,
 primary key(`modulId`),
 constraint `FK_Modul_Kurs` foreign key (`kursId`) references `Kurs` (`kursId`),
 constraint `FK_Modul_Forelser` foreign key (`foreleserId`) references `Foreleser` (`id`)
 );
 
 CREATE TABLE if not exists `Innlevering` (
  `innlevId` int(11) NOT NULL AUTO_INCREMENT,
  `modulId` int (11) NOT NULL,
  `fileName` varchar(50),
  `fileData` LONGBLOB,
  `id` int (11) NOT NULL,
  `gruppeId` int (11),
  `innlevKommentar` varchar (250),
  `innlevPoeng` int,
  primary key(`innlevId`),
  Constraint `FK_ModulListe_Innlevering` Foreign Key (`modulId`) references `Modul` (`modulId`) ON DELETE CASCADE,
  Constraint `FK_Bruker_Innlevering` Foreign Key (`id`) references `Bruker` (`id`) ON DELETE CASCADE, /*Ingen funksjon for å slette en innlevering*/
  Constraint `FK_Gruppe_Innlevering` Foreign Key (`gruppeId`) references `Gruppe` (`gruppeId`) ON DELETE CASCADE
  );
  
  CREATE TABLE if not exists `Kommentarer` (
  `komId` int(11) NOT NULL AUTO_INCREMENT,
  `id` int (11) NOT NULL,
  `innlevId` int(11) NOT NULL,
  `komKommentar` varchar (500) NOT NULL,
  `komTimestamp` timestamp NOT NULL,
  primary key(`komId`),
  Constraint `FK_Kommentarer_Bruker` Foreign Key (`id`) references `Bruker` (`id`) ON DELETE CASCADE,
  Constraint `FK_Kommentarer_Innlev` Foreign Key (`innlevId`) references `Innlevering` (`innlevId`) ON DELETE CASCADE
  );

CREATE TABLE `TarKurs` (
  `kursId` varchar(11) Not Null,
  `studentId` int(11) Not null,
  primary key(`kursId`,`studentId`),
  Constraint `FK_TarKurs_Kurs` foreign key (`kursId`) references `kurs` (`kursId`) ON DELETE CASCADE,
  Constraint `FK_TarKurs_Student` foreign key (`studentId`) references `Student` (id) ON DELETE CASCADE
);

CREATE TABLE `ForeleserKurs` (
  `kursId` varchar(11) not null,
  `foreleserId` int(11) not null,
  primary key(`kursId`,`foreleserId`),
  Constraint `FK_ForeleserKurs_Kurs` foreign key (`kursId`) references `kurs` (`kursId`) ON DELETE CASCADE,
  Constraint `FK_ForeleserKurs_Foreleser` foreign key (`foreleserId`) references `foreleser` (id) ON DELETE CASCADE
);

CREATE TABLE `Kunngjøringer` (
  `kunngjøringId` int(11) not null AUTO_INCREMENT,
  `kunngjøring` varchar(500) not null,
  `kursId` varchar(11) not null,
  `foreleserId` int(11) not null,
  `dato` varchar(25) not null,
  primary key (`kunngjøringId`),
  Constraint `FK_Kunngjøringer_Foreleser` foreign key (`foreleserId`) references `foreleser` (id) ON DELETE CASCADE,
  Constraint `FK_Kunngjøringer_Kurs` foreign key (`kursId`) references `kurs` (kursId) ON DELETE CASCADE
);

CREATE TABLE if not exists `Notifikasjoner` (
	`notId` int(11) NOT NULL AUTO_INCREMENT,
    `mottakerId` int(11) NOT NULL,
    `senderId` int(11) NOT NULL,
    `notUlest` boolean default true NOT null,
    `notType`	varchar(50) NOT null default '',
    `notReferererId` varchar(50) NOT null,
    `notOpprettet` timestamp Not Null,
    primary key (`notId`),
    constraint `FK_Notifikasjon_Bruker` foreign key(`mottakerId`) references `bruker`(`id`) ON DELETE CASCADE
);
CREATE TABLE `KalenderHendelse`(
	`hendelseId` int(11) not null auto_increment,
    `mottakerId` int(11) not null,
    `hendelseType` varchar(50) not null,
    `hendelseRef` varchar(50) not null,
    `hendelseTekst` varchar(200) not null,
    `hendelseStartDato` timestamp not null,
    `hendelseSluttDato` timestamp not null,
    primary key(`hendelseID`),
    constraint `FK_Kalenderhendelse_Bruker` foreign key(`mottakerId`) references `bruker` (`id`) ON DELETE CASCADE
);
