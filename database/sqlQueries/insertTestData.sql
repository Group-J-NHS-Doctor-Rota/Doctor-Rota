/*This data is completely made up*/

/*rotaGroups data*/
INSERT INTO rotaGroups (startDate, endDate, status)
VALUES
    ('2021-08-04', '2021-11-02', true);

/*accounts data*/
INSERT INTO accounts (name, password, salt, email, doctorId, accountStatus, doctorStatus, level, rotaGroupId, timeWorked, fixedWorking)
VALUES
    ('John Smith', 'sdafgndfjgn', '123', 'test@test.com', '9876', 0, 0, 1, 1, 1, false);

/*shifts data*/
INSERT INTO shifts (accountId, rotaGroupId, rotaTypeId, date, type, ruleNotes)
VALUES
    (1, 1, 1, '2021-09-01', 0, '');

/*leaveRequests data*/
INSERT INTO leaveRequests (accountId, date, type, note, status)
VALUES
    (1, '2021-09-07', 0, '', 0);

