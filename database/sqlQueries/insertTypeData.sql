/*Type data table map integers to names*/

/*levelTypes data*/
INSERT INTO levelTypes (id, name)
VALUES
    (0, 'Normal'),
    (1, 'Admin');

/*rotaTypes data*/
INSERT INTO rotaTypes (id, name)
VALUES
    (1, 'Type 1'),
    (2, 'Type 2'),
    (3, 'Type 3'),
    (4, 'Type 4');

/*shiftTypes data*/
INSERT INTO shiftTypes (id, name)
VALUES
    (0, 'Normal Working Day'),
    (1, 'Long Day'),
    (2, 'Night');

/*leaveRequestTypes data*/
INSERT INTO leaveRequestTypes (id, name)
VALUES
    (0, 'Annual Leave'),
    (1, 'Study Leave'),
    (2, 'Request Not To Be On Call');

/*statusTypes data*/
INSERT INTO statusTypes (id, name)
VALUES
    (0, 'Pending'),
    (1, 'Approved'),
    (2, 'Rejected');

/*notificationTypes data*/
INSERT INTO notificationTypes (id, name)
VALUES
    (0, 'Leave Request');