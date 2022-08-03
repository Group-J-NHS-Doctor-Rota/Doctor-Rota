/*All create tables in one sql file for ease of running and to enforce order*/

/*Rota groups are the 3 month long periods for which rotas are built*/
CREATE TABLE rotaGroups (
    id SERIAL NOT NULL,
    startDate date NOT NULL,
    endDate date NOT NULL,
    status bool NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Different possible levels of a account*/
CREATE TABLE levelTypes (
    id int NOT NULL,
    name varchar,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Account information for anyone using the website*/
CREATE TABLE accounts (
    id SERIAL NOT NULL,
    username varchar NOT NULL,
    --Only store encrypted and never actual password
    password varchar NOT NULL,
    salt varchar,
    email varchar,
    -- phone is varchar so it copes regardless of format
    phone varchar,
    doctorId varchar,
    annualLeave int DEFAULT 30,
    studyLeave int DEFAULT 15,
    workingHours int DEFAULT 48,
    accountStatus int,
    doctorStatus int,
    level int DEFAULT 0,
    rotaGroupId int,
    timeWorked float DEFAULT 1,
    fixedWorking bool DEFAULT false,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    UNIQUE (username),
    FOREIGN KEY (rotaGroupId) REFERENCES rotagroups(id),
    FOREIGN KEY (level) REFERENCES levelTypes(id)
);

/*Information on the 4 different rotas within each rota group*/
CREATE TABLE rotaTypes (
    id int NOT NULL,
    name varchar,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Different shift types*/
CREATE TABLE shiftTypes (
    id int NOT NULL,
    name varchar,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Information on rota shifts*/
CREATE TABLE shifts (
    id SERIAL NOT NULL,
    accountId int NOT NULL,
    rotaGroupId int NOT NULL,
    rotaTypeId int NOT NULL,
    date date NOT NULL,
    type int NOT NULL,
    ruleNotes varchar,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    UNIQUE (accountId, rotaTypeId, rotaGroupId, date),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (rotaGroupId) REFERENCES rotagroups(id),
    FOREIGN KEY (rotaTypeId) REFERENCES rotaTypes(id),
    FOREIGN KEY (type) REFERENCES shiftTypes(id)
);

/*Different types of leave requests*/
CREATE TABLE leaveRequestTypes (
    id SERIAL NOT NULL,
    name varchar,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Different types of request status*/
CREATE TABLE statusTypes (
    id int NOT NULL,
    name varchar,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Different types of request lengths*/
CREATE TABLE lengthTypes (
    id int NOT NULL,
    name varchar,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Details about days off and whether they are confirmed*/
CREATE TABLE leaveRequests (
    id SERIAL NOT NULL,
    accountId int NOT NULL,
    date date NOT NULL,
    type int NOT NULL,
    -- length type as not always a full day
    -- int is not the length but the key for length varchar
    length int NOT NULL DEFAULT 0,
    note varchar,
    status int NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    UNIQUE (accountId, date),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (type) REFERENCES leaveRequestTypes(id),
    FOREIGN KEY (length) REFERENCES lengthTypes(id),
    FOREIGN KEY (status) REFERENCES statusTypes(id)
);

/*Details about what leave requests are addressed by who*/
CREATE TABLE accountLeaveRequestRelationships (
    accountId int NOT NULL,
    leaveRequestId int NOT NULL,
    status int NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (accountId, leaveRequestId),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (leaveRequestId) REFERENCES leaveRequests(id),
    FOREIGN KEY (status) REFERENCES statusTypes(id)
);

/*Details about which accounts are working in which rota types for what period of days*/
CREATE TABLE accountRotaTypes (
    id SERIAL NOT NULL,
    accountId int NOT NULL,
    rotaTypeId int NOT NULL,
    startDate date NOT NULL,
    endDate date NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (rotaTypeId) REFERENCES rotaTypes(id)
);

/*Details on fixed rota shifts, for any accounts that have them*/
CREATE TABLE fixedRotaShifts (
    id SERIAL NOT NULL,
    accountId int NOT NULL,
    date date NOT NULL,
    shiftType int NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    UNIQUE (accountId, date),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (shiftType) REFERENCES shiftTypes(id)
);

/*Details on when part time works do or don't work*/
CREATE TABLE partTimeDetails (
    accountId int NOT NULL,
    monday bool NOT NULL,
    tuesday bool NOT NULL,
    wednesday bool NOT NULL,
    thursday bool NOT NULL,
    friday bool NOT NULL,
    saturday bool NOT NULL,
    sunday bool NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (accountId),
    FOREIGN KEY (accountId) REFERENCES accounts(id)
);

/*Different types of possible notification*/
CREATE TABLE notificationTypes (
    id int NOT NULL,
    name varchar NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Details on any notifications for users*/
CREATE TABLE notifications (
    id SERIAL NOT NULL,
    type int NOT NULL,
    detailId int NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    UNIQUE(type, detailId),
    FOREIGN KEY (type) REFERENCES notificationTypes(id)
);

/*Location of authentication tokens for each account*/
CREATE TABLE tokens (
    accountId int NOT NULL,
    token varchar NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (accountId),
    FOREIGN KEY (accountId) REFERENCES accounts(id)
);