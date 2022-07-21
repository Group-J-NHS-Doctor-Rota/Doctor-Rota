/*This data is completely made up*/

/*rotaGroups data*/
INSERT INTO rotaGroups (startDate, endDate, status)
VALUES
    ('2021-08-04', '2021-11-02', true);

/*accounts data*/
INSERT INTO accounts (username, password, salt, email, phone, doctorId, annualLeave, studyLeave, workingHours, accountStatus, doctorStatus, level, rotaGroupId, timeWorked, fixedWorking)
VALUES
    ('John Smith', 'sdafgndfjgn', '123', 'test@test.com', '07777 777 777', '9876', 15, 15, 48, 0, 0, 1, 1, 1, false);
INSERT INTO accounts (username, password, salt, email, annualLeave, studyLeave, workingHours, level)
VALUES
    ('Jane Smith', 'qwererydf', '456', 'test2@test.com', 15, 15, 48, 0),
    ('User One', 'gsgfsdgfdsg', '111', 'user1@test.com', 11, 1, 48, 1),
    ('User Two', 'dfhdfghfg', '222', 'user2@test.com', 12, 15, 48, 0),
    ('User Three', '45y6ertyeh', '333', 'user3@test.com', 13, 15, 48, 0),
    ('User Four', '5e6u5hg', '444', 'user4@test.com', 14, 15, 48, 0),
    ('User Five', '6e5hr', '555', 'user5@test.com', 15, 15, 47, 0),
    ('User Six', 'dhfgh654hth545', '666', 'user6@test.com', 16, 15, 48, 0),
    ('User Seven', 'fdghd54yh5', '777', 'user7@test.com', 17, 15, 48, 0),
    ('User Eight', 'dhw54htdfh', '888', 'user8@test.com', 18, 10, 48, 0),
    ('User Nine', 'fghkfgk7k', '99999', 'user9@test.com', 19, 15, 48, 0),
    ('User Ten', 'l78iiyitui', '09876', 'user10@test.com', 20, 15, 48, 0);

/*shifts data*/
INSERT INTO shifts (accountId, rotaGroupId, rotaTypeId, date, type, ruleNotes)
VALUES
    (1, 1, 1, '2021-09-01', 0, ''),
    (1, 1, 2, '2022-08-01', 1, ''),
    (2, 1, 3, '2022-08-01', 2, ''),
    (3, 1, 4, '2022-08-01', 3, ''),
    (4, 1, 1, '2022-08-01', 0, ''),
    (5, 1, 2, '2022-08-01', 1, ''),
    (6, 1, 1, '2022-08-01', 2, ''),
    (6, 1, 1, '2022-08-02', 3, ''),
    (6, 1, 1, '2022-08-03', 0, ''),
    (6, 1, 1, '2022-08-04', 1, ''),
    (6, 1, 1, '2022-08-05', 2, ''),
    (6, 1, 2, '2022-08-06', 3, ''),
    (6, 1, 2, '2022-08-07', 0, ''),
    (6, 1, 2, '2022-08-08', 1, ''),
    (6, 1, 2, '2022-08-09', 2, ''),
    (7, 1, 4, '2022-08-01', 0, ''),
    (7, 1, 4, '2022-08-02', 0, ''),
    (7, 1, 4, '2022-08-03', 2, ''),
    (7, 1, 4, '2022-08-04', 0, ''),
    (7, 1, 4, '2022-08-05', 1, '');

/*leaveRequests data*/
INSERT INTO leaveRequests (accountId, date, type, length, note, status)
VALUES
    (1, '2021-09-07', 0, 0, '', 0),
    (1, '2021-09-08', 1, 0, '', 1),
    (1, '2021-09-09', 2, 1, '', 2),
    (1, '2021-09-10', 9, 2, '', 0),
    (2, '2021-09-13', 0, 0, '', 0);

/*accountLeaveRequestRelationShips data*/
INSERT INTO accountLeaveRequestRelationships (accountId, leaveRequestId, status)
VALUES
    (1, 2, 1);

/*accountRotaTypes data*/
INSERT INTO accountRotaTypes (accountId, rotaTypeId, rotaGroupId, startDate, endDate)
VALUES
    (1, 2, 1, '2021-08-04', '2021-11-02');

/*fixedRotaShifts data*/
INSERT INTO fixedRotaShifts (accountId, date, shiftType)
VALUES
    (1, '2021-10-01', 2);

/*partTimeDetails data*/
INSERT INTO partTimeDetails (accountid, monday, tuesday, wednesday, thursday, friday, saturday, sunday)
VALUES
    (1, true, false, true, false, true, false, true);

/*notifications data*/
INSERT INTO notifications (type, detailId)
VALUES
    (0, 1),
    (0, 2),
    (0, 3),
    (0, 4),
    (0, 5);

/*tokens data*/
INSERT INTO tokens (accountid, token)
VALUES
    (1, 'asdfqwer10hjklyuio20'),
    (2, 'bsdfqwer10hjklyuio20'),
    (3, 'csdfqwer10hjklyuio20'),
    (4, 'dsdfqwer10hjklyuio20'),
    (5, 'esdfqwer10hjklyuio20'),
    (6, 'fsdfqwer10hjklyuio20'),
    (7, 'gsdfqwer10hjklyuio20'),
    (8, 'hsdfqwer10hjklyuio20'),
    (9, 'isdfqwer10hjklyuio20'),
    (10, 'jsdfqwer10hjklyuio20'),
    (11, 'ksdfqwer10hjklyuio20'),
    (12, 'lsdfqwer10hjklyuio20');