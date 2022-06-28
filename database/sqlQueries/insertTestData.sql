/*This data is completely made up*/

/*rotaGroups data*/
INSERT INTO rotagroups (startdate, enddate, status)
VALUES
    ('2021-08-04', '2021-11-02', true);

/*accounts data*/
INSERT INTO accounts (name, password, salt, email, doctorid, accountstatus, doctorstatus, level, rotagroupid, timeworked, fixedworking)
VALUES
    ('John Smith', 'sdafgndfjgn', '123', 'test@test.com', '9876', 0, 0, 1, 1, 1, false);

/*shifts data*/
INSERT INTO shifts (accountid, rotagroupid, rotatypeid, date, type, rulenotes)
VALUES
    (1, 1, 1, '2021-09-01', 0, '');

/*leaveRequests data*/
INSERT INTO leaveRequests (accountid, date, type, note, status)
VALUES
    (1, '2021-09-07', 0, '', 0);

