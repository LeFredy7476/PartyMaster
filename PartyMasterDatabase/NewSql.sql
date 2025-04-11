CREATE DATABASE  if not exists  JeuxSociete;
USE JeuxSociete;
 
 
CREATE TABLE if not exists question (
id INT AUTO_INCREMENT PRIMARY KEY,
question VARCHAR(100)NOT NULL,
reponse1 VARCHAR(100)NOT NULL,
reponse2 VARCHAR(100)NOT NULL,
reponse3 VARCHAR(100)NOT NULL,
reponse4 VARCHAR(100)NOT NULL,
bonneReponse VARCHAR(100 ) NOT NULL,
typeQuestion VARCHAR(100)NOT NULL
);
INSERT INTO question(question,reponse1,reponse2,reponse3,reponse4,bonneReponse,typeQuestion)VALUES
('Nommer moi le créateur de harry potter?','Ghandi','J.K Rowling','Maurice Leblanc','phillipe','J.K Rowling','Générale'),
('La taille de la tour effeil?','300M','100cm','100Km','1mm','300M','Générale'),
('Lequel de ses pays ne se situe pas en afrique?','Cameroun','Algérie','Canada','Togo','Canada','Géographie'),
('Le pays dans lequel se situe la tour de pise?','Maroc','Japon','Corée Du Nord','Italie','Italie','Géographie'),
('Dans quel language l\'indentation est importante ?','Java','Python','JavaScript','C#','Python','Programmation'),
('Quel Language est utilisé pour la manipulation de base de donnée?','Kotlin','Sql','python','React','Sql','Programmation');
 
CREATE TABLE if not exists questionSpecial (
id INT AUTO_INCREMENT PRIMARY KEY,
question VARCHAR(100)NOT NULL,
reponse VARCHAR(100)NOT NULL,
NiveauReponse int NOT NULL
);
 
INSERT INTO questionSpecial(question,reponse,NiveauReponse)VALUES
('Quel est la capitale de france','paris',1),
('qui est le président avant poutine a la présidence de la Russie','Boris Eltsine',2),
('Quel sont les 14 premier chiffre de Pi','3,1415926535897',3);
 
 
CREATE TABLE if NOT exists historiqueGame(
id VARCHAR(8)NOT NULL,
creation LONG NOT NULL
);