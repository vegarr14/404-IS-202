
INSERT into bruker (brukernavn, passord) Values ('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('hans', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('test', aes_encrypt('test', 'domo arigato mr.roboto')),
('', aes_encrypt('', 'domo arigato mr.roboto'));

INSERT into Student values('1','Vegar','Ryen','emailxd','12345678'),
('2','Sondre','Hammersbøen','emailcx','87654321'),
('3','Erlend','Thorsen','mailmail','12344321');
                     
INSERT into Foreleser values ('4','Hans','Mr.olav','yo@yahoo.com','99999999'),
(5, 'Mr.Vegar','Professor Ryen','emailxd', 12345678);

insert into Kurs values('IS-200','Systemanalyse og systemutvikling','img/is-200.jpg','All the silent students/people.<br>I dette kurset lærer vi om scrum.'),
('IS-201','Datamodellering og databasesystemer','img/is-201.jpg','Velkommen til IS-201<br>I dette kurset lærer vi om many to many og one to one.'),
('IS-202','Programmeringsprosjekt','img/is-202.jpg','Velkommen til IS-202<br>JAVA > html/css');

INSERT INTO Modul values ('1','IS-202','4','1','You`re wondering who i am!',false,1,null),
('2','IS-202','4','2','machine or mannequin. With parts made in Japan, I am the modern man!',false,1,null);

insert into TarKurs values('IS-200','1'),
('IS-201','1'),
('IS-202','2');

insert into ForeleserKurs values('IS-200','4');
                 
INSERT ignore into Innlevering (`modulId`, `id`, `innlevKommentar`, `innlevPoeng`) 
values (1, 1, 'Funker ting? Hurra', 80),
(1, 2, 'Helsa hu murmur från mig nu da', 60),
(2, 1, 'Hihihihihi', 40),
(2, 1, 'nfldanf', 69);

INSERT ignore into Kommentarer (`id`, `innlevId`, `komKommentar`) 
values (4, 1, 'Helsa helsa masse');
                     
/* select * from modulListe; */
