/*This data is completely made up*/

/*rotaGroups data*/
INSERT INTO rotagroups (startdate, enddate, status)
VALUES
    ('2021-08-04', '2021-11-02', true);

/*accounts data*/
INSERT INTO accounts (name, password, salt, email, doctorid, accountstatus, doctorstatus, level, rotagroupid, timeworked, fixedworking)
VALUES
    ('John Smith', 'sdafgndfjgn', '123', 'test@test.com', '9876', 0, 0, 1, 1, 1, false);

/*rotaTypes data*/
INSERT INTO rotaTypes (name)
VALUES
    ('Type 1'),
    ('Type 2'),
    ('Type 3'),
    ('Type 4');

