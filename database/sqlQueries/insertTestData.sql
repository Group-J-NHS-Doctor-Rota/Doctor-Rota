/*This data is completely made up*/

/*rotaGroups data*/
INSERT INTO rotaGroups (startDate, endDate, status)
VALUES
    ('2021-08-04', '2021-11-02', true);

/*accounts data*/
INSERT INTO accounts (username, name, password, salt, email, doctorId, accountStatus, doctorStatus, level, rotaGroupId, timeWorked, fixedWorking)
VALUES
    ('J Man', 'John Smith', 'sdafgndfjgn', '123', 'test@test.com', '9876', 0, 0, 1, 1, 1, false);

/*shifts data*/
INSERT INTO shifts (accountId, rotaGroupId, rotaTypeId, date, type, ruleNotes)
VALUES
    (1, 1, 1, '2021-09-01', 0, '');

/*leaveRequests data*/
INSERT INTO leaveRequests (accountId, date, type, note, status)
VALUES
    (1, '2021-09-07', 0, '', 0),
    (1, '2021-09-08', 1, '', 1);

/*accountLeaveRequestRelationShips data*/
INSERT INTO accountLeaveRequestRelationShips (accountId, leaveRequestId, status)
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
INSERT INTO partTimeDetails (accountId, dayOfWeek, canWork)
VALUES
    (1, 0, true),
    (1, 1, true),
    (1, 2, false),
    (1, 3, true),
    (1, 4, true),
    (1, 5, true),
    (1, 6, true);


/*notifications data*/
INSERT INTO notifications (type, detailId)
VALUES
    (0, 1);