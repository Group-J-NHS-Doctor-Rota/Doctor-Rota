/*This data is completely made up*/

/*rotaGroups data*/
INSERT INTO rotaGroups (startDate, endDate, status)
VALUES
    ('2021-08-04', '2021-11-02', true);

/*accounts data*/
INSERT INTO accounts (username, password, salt, email, doctorId, annualLeave, studyLeave, workingHours, accountStatus, doctorStatus, level, rotaGroupId, timeWorked, fixedWorking)
VALUES
    ('John Smith', 'sdafgndfjgn', '123', 'test@test.com', '9876', 15, 15, 48, 0, 0, 1, 1, 1, false);
INSERT INTO accounts (username, password, salt, email, annualLeave, studyLeave, workingHours, level)
VALUES
    ('Jane Smith', 'qwererydf', '456', 'test2@test.com', 15, 15, 48, 0);

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
    (0, 1);