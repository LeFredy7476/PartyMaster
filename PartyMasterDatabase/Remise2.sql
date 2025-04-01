DROP DATABASE if EXISTS JeuxSociete;
CREATE DATABASE JeuxSociete;
USE JeuxSociete;
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
id INT AUTO_INCREMENT PRIMARY KEY,
nom VARCHAR(100) NOT NULL
);
INSERT INTO roles (nom) VALUES
   ('Voyante'),
   ('Sorcière'),
   ('Chasseur'),
   ('Cupidon'),                                                                                      
	('Gardien'),
   ('Capitaine'),
     ('loup blanc'),
   ('Villageois'),
    ('Villageois'),
    ('Villageois');
DROP TABLE if EXISTS question;
CREATE TABLE question (
id INT AUTO_INCREMENT PRIMARY KEY,
question VARCHAR(100)NOT NULL,
reponse1 VARCHAR(100)NOT NULL,
reponse2 VARCHAR(100)NOT NULL,
reponse3 VARCHAR(100)NOT NULL,
reponse4 VARCHAR(100)NOT NULL,
typeQuestion VARCHAR(100 ) NOT NULL
);
INSERT INTO question(question,reponse1,reponse2,reponse3,reponse4,typeQuestion)VALUES
('Nommer moi le créateur de harry potter','J.K Rowling','Ghandi','Maurice Leblanc','phillipe','Générale'),
('La taille de la tour effeil','300M','100cm','100Km','1mm','Générale'),
('Lequel de ses pays ne se situe pas en afrique?','Canada','Algérie','Cameroun','Togo','Géographie'),
('Le pays dans lequel se situe la tour de pise','Italie','Japon','Corée Du Nord','Maroc','Géographie');
('Dans quel language l\'indentation est importante ?','Python','Java','JavaScript','Programmation'),
('Quel Language est utilisé pour les developpement d\application android?','Kotlin','Angular','python','React','Programmation');
