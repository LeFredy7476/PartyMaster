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
                            ('Petite Fille'),
                            ('Cupidon'),
                            ('Capitaine'),
                            ('loup blanc'),
                            ('Villageois'),
                            ('Villageois'),
                            ('Villageois');

DROP TABLE if EXISTS question;

CREATE TABLE question (
                          id INT AUTO_INCREMENT PRIMARY KEY,
                          question VARCHAR(100)NOT NULL,
                          reponse VARCHAR(100)NOT NULL
);

INSERT INTO question(question,reponse)VALUES
    ('Nommer moi le créateur de harry potter','J.K Rowling');

CREATE TABLE joueurs (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         nom VARCHAR(50) NOT NULL
);

INSERT INTO joueurs (nom) VALUES
                              ('Alice'),
                              ('Bob'),
                              ('Charlie'),
                              ('David'),
                              ('Emma'),
                              ('François'),
                              ('Gabrielle'),
                              ('Hugo'),
                              ('Isabelle'),
                              ('Julien'),
                              ('Kevin'),
                              ('Laura'),
                              ('Mathieu'),
                              ('Noémie');