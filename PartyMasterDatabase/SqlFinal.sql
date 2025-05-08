
CREATE DATABASE IF NOT EXISTS Jeuxsociete;
USE Jeuxsociete;

DROP table if EXISTS question;
CREATE TABLE IF NOT EXISTS question (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(100) NOT NULL,
    reponse1 VARCHAR(100) NOT NULL,
    reponse2 VARCHAR(100) NOT NULL,
    reponse3 VARCHAR(100) NOT NULL,
    reponse4 VARCHAR(100) NOT NULL,
    bonneReponse VARCHAR(100) NOT NULL,
    typeQuestion VARCHAR(100) NOT NULL
);

DROP table if EXISTS questionSpecial;
CREATE TABLE IF NOT EXISTS questionSpecial (
    id INT AUTO_INCREMENT PRIMARY KEY,
    question VARCHAR(100) NOT NULL,
    reponse VARCHAR(100) NOT NULL,
    NiveauReponse INT NOT NULL
);


CREATE TABLE IF NOT EXISTS historiqueGame (
    id VARCHAR(8) NOT NULL,
    creation LONG NOT NULL
);


DELETE FROM question;
DELETE FROM questionSpecial;


INSERT INTO question (question, reponse1, reponse2, reponse3, reponse4, bonneReponse, typeQuestion) VALUES

('Qui a cree la serie Harry Potter ?', 'Gandhi', 'J.K. Rowling', 'Maurice Leblanc', 'Philippe', 'J.K. Rowling', 'Generale'),
('Quelle est la hauteur de la Tour Eiffel ?', '300 m', '100 cm', '100 km', '1 mm', '300 m', 'Generale'),
('Quel est l element chimique symbole O ?', 'Or', 'Oxygene', 'Osmium', 'Oganesson', 'Oxygene', 'Generale'),
('Quel est le plus grand mammifere terrestre ?', 'Elephant', 'Girafe', 'Rhinoceros', 'Baleine bleue', 'Baleine bleue', 'Generale'),
('Qui a peint la Joconde ?', 'Vincent van Gogh', 'Leonard de Vinci', 'Pablo Picasso', 'Claude Monet', 'Leonard de Vinci', 'Generale'),
('Quel est le plus long fleuve du monde ?', 'Amazone', 'Nil', 'Mississippi', 'Yangtse', 'Amazone', 'Generale'),
('Quel gaz est le plus abondant dans l atmosphere ?', 'Oxygene', 'Azote', 'Carbone', 'Helium', 'Azote', 'Generale'),
('Quel est le symbole chimique de l or ?', 'Au', 'Ag', 'Fe', 'Cu', 'Au', 'Generale'),
('Dans quel sport utilise-t-on un palet ?', 'Hockey sur glace', 'Curling', 'Polo', 'Golf', 'Hockey sur glace', 'Generale'),
('Quel est le plus grand ocean ?', 'Atlantique', 'Pacifique', 'Indien', 'Arctique', 'Pacifique', 'Generale'),
('Quel animal est le plus rapide sur terre ?', 'Lion', 'Guepard', 'Antilope', 'Autruche', 'Guepard', 'Generale'),
('Quel est le nom du dieu grec du tonnerre ?', 'Zeus', 'Poseidon', 'Hades', 'Apollon', 'Zeus', 'Generale'),
('Quel est le plus grand desert du monde ?', 'Sahara', 'Antarctique', 'Gobi', 'Kalahari', 'Antarctique', 'Generale'),
('Quel est le plus haut sommet du monde ?', 'K2', 'Everest', 'Mont Blanc', 'Kilimandjaro', 'Everest', 'Generale'),
('Quel est l inventeur de la lampe a incandescence ?', 'Thomas Edison', 'Nikola Tesla', 'Alexander Bell', 'Guglielmo Marconi', 'Thomas Edison', 'Generale'),
('Quel est le nom de la premiere femme astronaute ?', 'Sally Ride', 'Valentina Terechkova', 'Mae Jemison', 'Christa McAuliffe', 'Valentina Terechkova', 'Generale'),
('Quel est le plus grand pays par superficie ?', 'Canada', 'Chine', 'Russie', 'Etats-Unis', 'Russie', 'Generale'),
('Quel est le nom du premier homme sur la Lune ?', 'Buzz Aldrin', 'Neil Armstrong', 'Yuri Gagarine', 'Alan Shepard', 'Neil Armstrong', 'Generale'),
('Quel est le plus grand reptile vivant ?', 'Crocodile', 'Python', 'Tortue geante', 'Dragon de Komodo', 'Crocodile', 'Generale'),
('Quel est le nom du detective cree par Conan Doyle ?', 'Hercule Poirot', 'Sherlock Holmes', 'Philip Marlowe', 'Sam Spade', 'Sherlock Holmes', 'Generale'),
('Quel est le plus grand lac d eau douce ?', 'Lac Superieur', 'Lac Baikal', 'Lac Victoria', 'Lac Titicaca', 'Lac Superieur', 'Generale'),
('Quel est le nom de la reine d Angleterre au pouvoir le plus longtemps ?', 'Victoria', 'Elisabeth II', 'Anne', 'Marie', 'Elisabeth II', 'Generale'),
('Quel est le metal le plus leger ?', 'Aluminium', 'Lithium', 'Titane', 'Magnesium', 'Lithium', 'Generale'),
('Quel est le nom du premier ordinateur personnel ?', 'Altair 8800', 'Apple I', 'IBM PC', 'Commodore 64', 'Altair 8800', 'Generale'),
('Quel est le plus grand poisson ?', 'Requin-baleine', 'Requin blanc', 'Thon', 'Manta', 'Requin-baleine', 'Generale'),
('Quel est le nom du tableau celebre de Van Gogh ?', 'La Nuit etoilee', 'Les Tournesols', 'Le Cafe de nuit', 'L Homme a l oreille coupee', 'La Nuit etoilee', 'Generale'),
('Quel est le plus grand oiseau incapable de voler ?', 'Autruche', 'Emeu', 'Casoar', 'Nandou', 'Autruche', 'Generale'),
('Quel est le nom du dieu egyptien du soleil ?', 'Osiris', 'Ra', 'Anubis', 'Horus', 'Ra', 'Generale'),
('Quel est le plus grand stade de football du monde ?', 'Camp Nou', 'Maracana', 'Wembley', 'Rungrado', 'Rungrado', 'Generale'),
('Quel est le nom du premier satellite artificiel ?', 'Spoutnik 1', 'Explorer 1', 'Vanguard 1', 'Luna 2', 'Spoutnik 1', 'Generale'),
('Quel est le plus grand arbre du monde ?', 'Sequoia geant', 'Baobab', 'Pin ponderosa', 'Eucalyptus', 'Sequoia geant', 'Generale'),
('Quel est le nom du compositeur de la 5e symphonie ?', 'Mozart', 'Beethoven', 'Bach', 'Tchaikovski', 'Beethoven', 'Generale'),
('Quel est le plus grand musee du monde ?', 'Louvre', 'Metropolitan', 'Ermitage', 'British Museum', 'Louvre', 'Generale'),
('Quel est le nom du premier film de Disney ?', 'Blanche-Neige', 'Pinocchio', 'Cendrillon', 'Dumbo', 'Blanche-Neige', 'Generale'),


('Quel pays ne se situe pas en Afrique ?', 'Cameroun', 'Algerie', 'Canada', 'Togo', 'Canada', 'Geographie'),
('Dans quel pays se trouve la tour de Pise ?', 'Maroc', 'Japon', 'Coree du Nord', 'Italie', 'Italie', 'Geographie'),
('Quel est le plus petit pays du monde ?', 'Monaco', 'Vatican', 'Maldives', 'Liechtenstein', 'Vatican', 'Geographie'),
('Quelle est la capitale du Japon ?', 'Tokyo', 'Kyoto', 'Osaka', 'Hiroshima', 'Tokyo', 'Geographie'),
('Quel pays a le plus d habitants ?', 'Inde', 'Chine', 'Etats-Unis', 'Indonesie', 'Chine', 'Geographie'),
('Quel est le plus grand desert de sable ?', 'Sahara', 'Gobi', 'Kalahari', 'Atacama', 'Sahara', 'Geographie'),
('Quelle est la capitale du Bresil ?', 'Rio de Janeiro', 'Sao Paulo', 'Brasilia', 'Salvador', 'Brasilia', 'Geographie'),
('Dans quel continent se trouve l Australie ?', 'Asie', 'Oceanie', 'Afrique', 'Amerique', 'Oceanie', 'Geographie'),
('Quel pays est surnomme le pays du Soleil-Levant ?', 'Chine', 'Japon', 'Coree du Sud', 'Thailande', 'Japon', 'Geographie'),
('Quelle est la capitale de l Egypte ?', 'Le Caire', 'Alexandrie', 'Gizeh', 'Louxor', 'Le Caire', 'Geographie'),
('Quel est le plus grand archipel du monde ?', 'Indonesie', 'Philippines', 'Maldives', 'Japon', 'Indonesie', 'Geographie'),
('Quelle est la capitale de la Russie ?', 'Moscou', 'Saint-Petersbourg', 'Kazan', 'Novossibirsk', 'Moscou', 'Geographie'),
('Quel pays borde le golfe Persique ?', 'France', 'Arabie saoudite', 'Chili', 'Australie', 'Arabie saoudite', 'Geographie'),
('Quelle est la capitale de l Inde ?', 'Mumbai', 'Delhi', 'Kolkata', 'Bangalore', 'Delhi', 'Geographie'),
('Quel est le plus grand lac d Afrique ?', 'Lac Victoria', 'Lac Tanganyika', 'Lac Malawi', 'Lac Tchad', 'Lac Victoria', 'Geographie'),
('Quelle est la capitale de l Argentine ?', 'Buenos Aires', 'Cordoba', 'Rosario', 'Mendoza', 'Buenos Aires', 'Geographie'),
('Quel pays est connu pour ses fjords ?', 'Norvege', 'Islande', 'Suede', 'Finlande', 'Norvege', 'Geographie'),
('Quelle est la capitale du Canada ?', 'Toronto', 'Vancouver', 'Ottawa', 'Montreal', 'Ottawa', 'Geographie'),
('Quel est le plus grand pays d Amerique du Sud ?', 'Bresil', 'Argentine', 'Perou', 'Colombie', 'Bresil', 'Geographie'),
('Quelle est la capitale de l Australie ?', 'Sydney', 'Melbourne', 'Canberra', 'Brisbane', 'Canberra', 'Geographie'),
('Quel pays est surnomme la Terre des Kangourous ?', 'Nouvelle-Zelande', 'Australie', 'Afrique du Sud', 'Indonesie', 'Australie', 'Geographie'),
('Quelle est la capitale de la Chine ?', 'Shanghai', 'Pekin', 'Hong Kong', 'Guangzhou', 'Pekin', 'Geographie'),
('Quel est le plus grand pays d Afrique ?', 'Algerie', 'Soudan', 'Nigeria', 'Ethiopie', 'Algerie', 'Geographie'),
('Quelle est la capitale de l Italie ?', 'Milan', 'Rome', 'Venise', 'Florence', 'Rome', 'Geographie'),
('Quel pays est connu pour ses tulipes ?', 'Belgique', 'Pays-Bas', 'Danemark', 'Allemagne', 'Pays-Bas', 'Geographie'),
('Quelle est la capitale de l Espagne ?', 'Barcelone', 'Madrid', 'Valence', 'Seville', 'Madrid', 'Geographie'),
('Quel est le plus grand pays d Asie ?', 'Chine', 'Inde', 'Russie', 'Japon', 'Russie', 'Geographie'),
('Quelle est la capitale de la Turquie ?', 'Istanbul', 'Ankara', 'Izmir', 'Bursa', 'Ankara', 'Geographie'),
('Quel pays est connu pour le mont Fuji ?', 'Chine', 'Japon', 'Coree', 'Vietnam', 'Japon', 'Geographie'),
('Quelle est la capitale du Mexique ?', 'Cancun', 'Mexico', 'Guadalajara', 'Monterrey', 'Mexico', 'Geographie'),
('Quel est le plus grand pays d Europe ?', 'France', 'Ukraine', 'Espagne', 'Suede', 'Ukraine', 'Geographie'),
('Quelle est la capitale de la Coree du Sud ?', 'Seoul', 'Busan', 'Incheon', 'Daegu', 'Seoul', 'Geographie'),
('Quel pays est connu pour ses pyramides ?', 'Mexique', 'Egypte', 'Soudan', 'Perou', 'Egypte', 'Geographie'),


('Dans quel langage l indentation est-elle importante ?', 'Java', 'Python', 'JavaScript', 'C#', 'Python', 'Programmation'),
('Quel langage est utilise pour manipuler les bases de donnees ?', 'Kotlin', 'SQL', 'Python', 'React', 'SQL', 'Programmation'),
('Quel langage est principalement utilise pour le web ?', 'Python', 'JavaScript', 'C++', 'Ruby', 'JavaScript', 'Programmation'),
('Quel est le langage cree par Guido van Rossum ?', 'Python', 'Java', 'C', 'Perl', 'Python', 'Programmation'),
('Quel langage utilise la JVM ?', 'Python', 'Java', 'JavaScript', 'C#', 'Java', 'Programmation'),
('Quel est le langage de programmation le plus ancien ?', 'C', 'Fortran', 'Java', 'Python', 'Fortran', 'Programmation'),
('Quel langage est utilise pour le developpement Android ?', 'Swift', 'Kotlin', 'Objective-C', 'PHP', 'Kotlin', 'Programmation'),
('Quel langage est associe a la bibliotheque React ?', 'JavaScript', 'Python', 'Java', 'C++', 'JavaScript', 'Programmation'),
('Quel langage utilise des pointeurs explicitement ?', 'Python', 'C++', 'Java', 'Ruby', 'C++', 'Programmation'),
('Quel est le langage principal pour iOS ?', 'Swift', 'Kotlin', 'Java', 'C#', 'Swift', 'Programmation'),
('Quel langage est utilise pour les scripts cote serveur ?', 'PHP', 'HTML', 'CSS', 'SQL', 'PHP', 'Programmation'),
('Quel langage est oriente objets et statiquement type ?', 'Python', 'Java', 'JavaScript', 'Ruby', 'Java', 'Programmation'),
('Quel langage est utilise pour les bases NoSQL comme MongoDB ?', 'SQL', 'JavaScript', 'Python', 'C#', 'JavaScript', 'Programmation'),
('Quel langage est connu pour sa syntaxe concise ?', 'Ruby', 'Java', 'C++', 'PHP', 'Ruby', 'Programmation'),
('Quel langage est utilise pour le machine learning ?', 'Python', 'C', 'Java', 'PHP', 'Python', 'Programmation'),
('Quel langage est compile en bytecode ?', 'Java', 'Python', 'JavaScript', 'Go', 'Java', 'Programmation'),
('Quel langage est utilise pour les jeux avec Unity ?', 'C#', 'Python', 'Java', 'C++', 'C#', 'Programmation'),
('Quel langage est utilise pour les scripts shell ?', 'Bash', 'Python', 'Java', 'C', 'Bash', 'Programmation'),
('Quel langage est associe a la bibliotheque Pandas ?', 'Python', 'R', 'Java', 'C++', 'Python', 'Programmation'),
('Quel langage est utilise pour le developpement web backend ?', 'Python', 'HTML', 'CSS', 'Ruby', 'Ruby', 'Programmation'),
('Quel langage est utilise pour les smart contracts ?', 'Solidity', 'Python', 'Java', 'C#', 'Solidity', 'Programmation'),
('Quel langage est utilise pour les bases Oracle ?', 'SQL', 'PL/SQL', 'Python', 'Java', 'PL/SQL', 'Programmation'),
('Quel langage est utilise pour le developpement de jeux ?', 'C++', 'Python', 'Java', 'PHP', 'C++', 'Programmation'),
('Quel langage est connu pour sa gestion des threads ?', 'Java', 'Python', 'JavaScript', 'Ruby', 'Java', 'Programmation'),
('Quel langage est utilise pour les applications Flutter ?', 'Dart', 'Python', 'Java', 'C#', 'Dart', 'Programmation'),
('Quel langage est utilise pour les bases PostgreSQL ?', 'SQL', 'Python', 'Java', 'C', 'SQL', 'Programmation'),
('Quel langage est associe a la bibliotheque NumPy ?', 'Python', 'R', 'Java', 'C++', 'Python', 'Programmation'),
('Quel langage est utilise pour les scripts cote client ?', 'JavaScript', 'Python', 'Java', 'C++', 'JavaScript', 'Programmation'),
('Quel langage est utilise pour les bases MySQL ?', 'SQL', 'Python', 'Java', 'C', 'SQL', 'Programmation'),
('Quel langage est utilise pour le framework Django ?', 'Python', 'Java', 'PHP', 'Ruby', 'Python', 'Programmation'),
('Quel langage est utilise pour le framework Spring ?', 'Java', 'Python', 'C#', 'JavaScript', 'Java', 'Programmation'),
('Quel langage est utilise pour les bases SQLite ?', 'SQL', 'Python', 'Java', 'C', 'SQL', 'Programmation'),
('Quel langage est utilise pour les applications Angular ?', 'JavaScript', 'Python', 'Java', 'C#', 'JavaScript', 'Programmation');


INSERT INTO questionSpecial (question, reponse, NiveauReponse) VALUES

('Quelle est la capitale de la France ?', 'Paris', 1),
('Quel est le plus grand ocean du monde ?', 'Pacifique', 1),
('Quel est le symbole chimique de l eau ?', 'H2O', 1),
('Quel est le nom du plus grand pays d Afrique ?', 'Algerie', 1),
('Quel est le plus haut sommet d Europe ?', 'Mont Blanc', 1),
('Quel est le nom du dieu grec de la guerre ?', 'Ares', 1),
('Quelle est la capitale de l Allemagne ?', 'Berlin', 1),
('Quel est le nom du plus grand desert de sable ?', 'Sahara', 1),
('Quel est le nom du plus grand fleuve d Europe ?', 'Volga', 1),


('Qui etait le president de la Russie avant Poutine ?', 'Boris Eltsine', 2),
('Quel est le nom du plus grand lac d Amerique du Sud ?', 'Titicaca', 2),
('Quel est le nom du plus grand volcan actif ?', 'Mauna Loa', 2),
('Quel est le nom du premier homme a orbiter la Terre ?', 'Youri Gagarine', 2),
('Quel est le nom du plus grand pont suspendu ?', 'Golden Gate', 2),
('Quel est le nom de la plus grande ile de la Mediterranee ?', 'Sicile', 2),
('Quel est le nom du plus grand animal terrestre eteint ?', 'Argentinosaurus', 2),
('Quel est le nom du plus grand stade d Europe ?', 'Camp Nou', 2),


('Quels sont les 14 premiers chiffres de Pi ?', '3,1415926535897', 3),
('Quel est le nom du plus ancien calendrier encore utilise ?', 'Calendrier hebraique', 3),
('Quel est le nom du plus grand glacier du monde ?', 'Lambert-Fisher', 3),
('Quel est le nom du premier homme a atteindre le pole Sud ?', 'Roald Amundsen', 3),
('Quel est le nom du plus grand canyon sous-marin ?', 'Canyon de Zhemchug', 3),
('Quel est le nom du plus ancien instrument de musique ?', 'Flute de Divje Babe', 3),
('Quel est le nom du plus grand asteroide connu ?', 'Ceres', 3),
('Quel est le nom du plus ancien texte litteraire ?', 'Epopee de Gilgamesh', 3);

