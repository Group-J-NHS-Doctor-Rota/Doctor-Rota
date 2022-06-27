/*Account information for anyone using the website*/

CREATE TABLE accounts (
    id SERIAL NOT NULL,
    name varchar NOT NULL,
    --Only store encrypted and never actual password
    password varchar NOT NULL,
    salt varchar NOT NULL,
    email varchar,
    doctorId varchar NOT NULL,
    accountStatus int NOT NULL,
    doctorStatus int NOT NULL,
    level int NOT NULL,
    rotaGroupId int,
    timeWorked float NOT NULL,
    fixedWorking bool NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (rotaGroupId) REFERENCES rotagroups(id)
);