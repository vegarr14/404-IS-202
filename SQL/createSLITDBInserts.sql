INSERT into bruker (`brukernavn`, `passord`)
Values ('', aes_encrypt('', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('ErleTho17', aes_encrypt('test', 'domo arigato mr.roboto')),
('hans', aes_encrypt('test', 'domo arigato mr.roboto')),
('DeviTha10', aes_encrypt('test', 'domo arigato mr.roboto')),
('HallNil00', aes_encrypt('test', 'domo arigato mr.roboto')),
('EvenLar00', aes_encrypt('test', 'domo arigato mr.roboto'));

INSERT ignore into Student (`id`, `forNavn`, `etterNavn`, `email`, `tlf`)
values('1','Vegar','Ryen','emailxd','12345678'),
('2','Sondre','Hammersbøen','emailcx','87654321'),
('3','Erlend','Thorsen','mailmail','12344321');
                     
INSERT into Foreleser (`id`, `forNavn`, `etterNavn`, `email`, `tlf`)
values ('4','Hans Olav','Omland','hans.o.omland@uia.no','38141651'),
('5','Devinder','Thapa','devinder.thapa@uia.no','38141419'),
('6','Even','Larsen','even.larsen@uia.no','38141619'),
('7', 'Hallgeir','Nilsen','hallgeir.nilsen@uia.no', '38141585');

insert into Kurs values('IS-200','Systemanalyse og systemutvikling','img/is-200.jpg','All the silent students/people.<br>I dette kurset lærer vi om scrum.'),
('IS-201','Datamodellering og databasesystemer','img/is-201.jpg','Velkommen til IS-201<br>I dette kurset lærer vi om many to many og one to one.'),
('IS-202','Programmeringsprosjekt','img/is-202.jpg','Velkommen til IS-202<br>JAVA > html/css');

INSERT INTO Modul (`modulId`, `kursId`, `foreleserId`, `modulNummer`, `oppgaveTekst`, levereSomGruppe, maxPoeng, innleveringsFrist) 
values ('1','IS-202','6','1','You`re wondering who i am!', false, 100, null),
('2','IS-202','7','2','machine or mannequin. With parts made in Japan, I am the modern man!', false, 100, null),
('3','IS-202','6','3','You`re wondering who i am!', false, 100, null),
('4','IS-202','7','4','♪	MAN. MACHINE. THE FUTURE, FUTURE...!♪	', false, 100, null),
('5','IS-200','4','1','You`re wondering who i am! ', false, 100, null),
('6','IS-200','4','4','You`re wondering who i am!', false, 100, null),
('10','IS-201','5','1','You`re wondering who i am!', false, 100, null),
('11','IS-201','5','2','You`re wondering who i am!', false, 100, null);

insert into TarKurs values('IS-200','1'),
('IS-201','1'),
('IS-202','1'),
('IS-200','2'),
('IS-201','2'),
('IS-202','2'),
('IS-200','3'),
('IS-201','3'),
('IS-202','3');

insert into ForeleserKurs values('IS-200','4'), 
('IS-201','4'), 
('IS-202','4'),
('IS-201','5'),
('IS-202','6'),
('IS-202','7');
                 
INSERT into Innlevering (`modulId`, `id`, `innlevKommentar`, `innlevPoeng`) 
values (1, 1, 'Funker ting? Hurra', 70),
(1, 2, 'Helsa hu murmur från mig nu da', 50),
(2, 1, 'Helsa hu murmur från mig nu da', null),
(4, 2, 'Helsa hu murmur från mig nu da', 19),
(2, 3, 'Helsa hu murmur från mig nu da', null),
(10, 1,'Helsa hu murmur från mig nu da', null),
(11, 2,'Helsa hu murmur från mig nu da', 83);

INSERT ignore into Kommentarer (`id`, `innlevId`, `komKommentar`) 
values (4, 1, 'Helsa helsa masse');
                     
/* select * from modulListe; */
