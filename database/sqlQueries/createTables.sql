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
    -- Might not need name?
    name varchar,
    --Only store encrypted and never actual password
    password varchar NOT NULL,
    salt varchar NOT NULL,
    email varchar,
    doctorId varchar NOT NULL,
    annualLeave int NOT NULL,
    studyLeave int NOT NULL,
    workingHours int NOT NULL,
    accountStatus int NOT NULL,
    doctorStatus int NOT NULL,
    level int NOT NULL,
    rotaGroupId int,
    timeWorked float NOT NULL,
    fixedWorking bool NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
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

/*Details about days off and whether they are confirmed*/
CREATE TABLE leaveRequests (
    id SERIAL NOT NULL,
    accountId int NOT NULL,
    date date NOT NULL,
    type int NOT NULL,
    note varchar,
    status int NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (type) REFERENCES leaveRequestTypes(id),
    FOREIGN KEY (status) REFERENCES statusTypes(id)
);

/*Details about what leave requests are addressed by who*/
CREATE TABLE accountLeaveRequestRelationShips (
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
    rotaGroupId int NOT NULL,
    startDate date NOT NULL,
    endDate date NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    UNIQUE (accountId, rotaTypeId, rotaGroupId),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (rotaTypeId) REFERENCES rotaTypes(id),
    FOREIGN KEY (rotaGroupId) REFERENCES rotaGroups(id)
);

/*Details on fixed rota shifts, for any accounts that have them*/
CREATE TABLE fixedRotaShifts (
    id SERIAL NOT NULL,
    accountId int NOT NULL,
    date date NOT NULL,
    shiftType int NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (shiftType) REFERENCES shiftTypes(id)
);

/*Int to days of the week*/
CREATE TABLE dayOfWeek (
    id int NOT NULL,
    day varchar NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);

/*Details on when part time works do or don't work*/
CREATE TABLE partTimeDetails (
    id SERIAL NOT NULL,
    accountId int NOT NULL,
    dayOfWeek int NOT NULL,
    canWork bool NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (accountId) REFERENCES accounts(id),
    FOREIGN KEY (dayOfWeek) REFERENCES dayOfWeek(id)
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
    FOREIGN KEY (type) REFERENCES notificationTypes(id)
);
