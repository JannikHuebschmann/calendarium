CREATE TABLE person
(
	kuerzel VARCHAR(10) DEFAULT '' NOT NULL,
	vorname VARCHAR(32),
	nachname VARCHAR(32),
	PRIMARY KEY (kuerzel)
);

CREATE TABLE passwort (
	kuerzel VARCHAR(10) DEFAULT '' NOT NULL,
	passwort VARCHAR(32),
	PRIMARY KEY (kuerzel)
);

CREATE TABLE eintrag
(
	DTYPE VARCHAR(31) DEFAULT '' NOT NULL,
	id INT NOT NULL AUTO_INCREMENT,
	kurzText VARCHAR(64),
	langText VARCHAR(255),
	beginn TIMESTAMP,
	ende   TIMESTAMP,
	verschiebbar BIT,
	besitzer_kuerzel VARCHAR(10),
	PRIMARY KEY (id)
);

CREATE TABLE eintrag_person
(
	Eintrag_id INT NOT NULL,
	teilnehmer_kuerzel VARCHAR(10) NOT NULL
);

CREATE INDEX FKI_besitzer ON eintrag (besitzer_kuerzel ASC);
CREATE INDEX FKI_teilnehmer ON eintrag_person (teilnehmer_kuerzel ASC);
CREATE INDEX FKI_eintrag ON eintrag_person (Eintrag_id ASC);

ALTER TABLE eintrag ADD CONSTRAINT FK_besitzer FOREIGN KEY (besitzer_kuerzel)
	REFERENCES person (kuerzel)
	ON UPDATE CASCADE
	ON DELETE SET NULL;

ALTER TABLE eintrag_person ADD CONSTRAINT FK_eintrag FOREIGN KEY (Eintrag_id)
	REFERENCES eintrag (id)
	ON DELETE CASCADE;

ALTER TABLE eintrag_person ADD CONSTRAINT FK_teilnehmer FOREIGN KEY (teilnehmer_kuerzel)
	REFERENCES person (kuerzel)
	ON UPDATE CASCADE
	ON DELETE CASCADE;

