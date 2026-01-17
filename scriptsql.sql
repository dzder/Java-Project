CREATE DATABASE IF NOT EXISTS `java-project`;
USE `java-project`;

CREATE TABLE utilisateur (
    idUtil INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    mdp VARCHAR(255) NOT NULL,
    role ENUM('admin','employe') NOT NULL
);

CREATE TABLE client (
    idClient INT AUTO_INCREMENT PRIMARY KEY,
    nomPrenom VARCHAR(100) NOT NULL,
    mail VARCHAR(100)
);

CREATE TABLE fournisseur (
    idFour INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    contact VARCHAR(100)
);

CREATE TABLE produit (
    idProduit INT AUTO_INCREMENT PRIMARY KEY,
    nomProduit VARCHAR(100) NOT NULL,
    prix DECIMAL(10,3) NOT NULL,
    quantite INT NOT NULL CHECK (quantite >= 0),
    seuilMinimal INT NOT NULL CHECK (seuilMinimal >= 0)
);

CREATE TABLE commandeFournisseur (
    idCommande INT AUTO_INCREMENT PRIMARY KEY,
    dateCommande DATE NOT NULL,
    statut ENUM('en_attente','recu','annule','finalise') NOT NULL,
    idFour INT NOT NULL,
    FOREIGN KEY (idFour) REFERENCES fournisseur(idFour)
);

CREATE TABLE vente (
    idVente INT AUTO_INCREMENT PRIMARY KEY,
    montantVente DECIMAL(10,3) NOT NULL,
    dateVente DATE NOT NULL,
    idUtil INT NOT NULL,
    idClient INT,
    FOREIGN KEY (idUtil) REFERENCES utilisateur(idUtil),
    FOREIGN KEY (idClient) REFERENCES client(idClient)
);

CREATE TABLE correspond (
    idVente INT NOT NULL,
    idProduit INT NOT NULL,
    quantite INT NOT NULL CHECK (quantite > 0),
    montant DECIMAL(10,3) NOT NULL,
    PRIMARY KEY (idVente, idProduit),
    FOREIGN KEY (idVente) REFERENCES vente(idVente),
    FOREIGN KEY (idProduit) REFERENCES produit(idProduit)
);

CREATE TABLE concerne (
    idCommande INT NOT NULL,
    idProduit INT NOT NULL,
    quantite INT NOT NULL CHECK (quantite > 0),
    montant DECIMAL(10,3) NOT NULL,
    PRIMARY KEY (idCommande, idProduit),
    FOREIGN KEY (idCommande) REFERENCES commandeFournisseur(idCommande),
    FOREIGN KEY (idProduit) REFERENCES produit(idProduit)
);

CREATE USER 'java_user'@'localhost' IDENTIFIED BY 'placeholder';
GRANT SELECT, INSERT, UPDATE, DELETE ON `java-project`.* TO 'java_user'@'localhost';
FLUSH PRIVILEGES;


INSERT INTO utilisateur (username, mdp, role) VALUES
('admin1','mdp_hash_admin','admin'),
('employe1','mdp_hash_employe','employe');

INSERT INTO client (nomPrenom, mail) VALUES
('Bowie David', 'David@gmail.com'),
('Ross Diana', 'Diana@example.com');

INSERT INTO fournisseur (nom, contact) VALUES
('Fournisseur A', 'contactA@gmail.com');

INSERT INTO produit (nomProduit, prix, quantite, seuilMinimal) VALUES
('Doliprane', 5.50, 100, 10),
('Fervex', 7.00, 50, 5),
('zyrtec', 12.00, 200, 20);

INSERT INTO commandeFournisseur (dateCommande, statut, idFour) VALUES
('2026-01-17', 'en_attente', 1);

INSERT INTO vente (montantVente, dateVente, idUtil, idClient) VALUES
(16.50, '2026-01-17', 2, 1);

INSERT INTO concerne (idCommande, idProduit, quantite, montant) VALUES
(1, 1, 50, 275.00);

INSERT INTO correspond (idVente, idProduit, quantite, montant) VALUES
(1, 1, 2, 11.00),
(1, 3, 1, 12.00);