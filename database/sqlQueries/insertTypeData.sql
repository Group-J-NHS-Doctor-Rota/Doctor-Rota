/*Type data table map integers to names*/

/*rotaTypes data*/
INSERT INTO rotaTypes (name)
VALUES
    ('Type 1'),
    ('Type 2'),
    ('Type 3'),
    ('Type 4');

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

/*dayOfWeek data*/
INSERT INTO dayOfWeek (id, day)
VALUES
    (0, 'Monday'),
    (1, 'Tuesday'),
    (2, 'Wednesday'),
    (3, 'Thursday'),
    (4, 'Friday'),
    (5, 'Saturday'),
    (6, 'Sunday');