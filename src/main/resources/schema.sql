DROP TABLE IF EXISTS CAR_ITEMS;

CREATE TABLE CAR_ITEMS (ID SERIAL, REGISTRATION CHARACTER VARYING (7) NOT NULL, MARK1 CHARACTER VARYING, MARK2 CHARACTER VARYING, DESCRIPTION CHARACTER VARYING NOT NULL, SOURCE CHARACTER VARYING, PRIMARY KEY(ID));