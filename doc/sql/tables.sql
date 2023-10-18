CREATE DATABASE db_kotlinAventure;

USE db_kotlinAventure;

CREATE TABLE Qualite
(
    id          INT AUTO_INCREMENT,
    nom         VARCHAR(255),
    bonusRarete INT,
    couleur     VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE TABLE TypeArme
(
    id                     INT AUTO_INCREMENT,
    nom                    VARCHAR(255) NOT NULL,
    nombreDes              INT,
    valeurDeMax            INT,
    multiplicateurCritique INT,
    activationCritique     INT,
    PRIMARY KEY (id)
);

CREATE TABLE TypeArmure
(
    id        INT AUTO_INCREMENT,
    nom       VARCHAR(255) NOT NULL,
    bonusType INT,
    PRIMARY KEY (id)
);

CREATE TABLE Arme
(
    id          INT AUTO_INCREMENT,
    nom         VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    id_qualite  INT          NOT NULL,
    id_type     INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_qualite) REFERENCES Qualite (id),
    FOREIGN KEY (id_type) REFERENCES TypeArme (id)
);

CREATE TABLE Armure
(
    id          INT AUTO_INCREMENT,
    nom         VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    id_type     INT          NOT NULL,
    id_qualite  INT          NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id_type) REFERENCES TypeArmure (id),
    FOREIGN KEY (id_qualite) REFERENCES Qualite (id)
);

CREATE TABLE Potion
(
    id          INT AUTO_INCREMENT,
    nom         VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    soin        INT,
    PRIMARY KEY (id)
);

CREATE TABLE Bombe
(
    id          INT AUTO_INCREMENT,
    nom         VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    nombreDeDes INT,
    maxDe       INT,
    PRIMARY KEY (id)
);
