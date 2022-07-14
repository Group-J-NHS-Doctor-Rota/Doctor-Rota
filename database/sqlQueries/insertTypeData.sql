/*Type data table map integers to names*/

/*levelTypes data*/
INSERT INTO levelTypes (id, name)
VALUES
    (0, 'Normal'),
    (1, 'Admin');

/*rotaTypes data*/
INSERT INTO rotaTypes (id, name)
VALUES
    (1, 'First On'),
    (2, 'Obstetrics'),
    (3, 'Second On'),
    (4, 'Third 0n');

/*shiftTypes data*/
INSERT INTO shiftTypes (id, name)
VALUES
    (0, 'Normal Working Day'),
    (1, 'Long Day'),
    (2, 'Night'),
    (3, 'Trainee Off Day'),
    --Gap is to allow for any additions, as other should logically come at the end
    (9, 'Other');

/*leaveRequestTypes data*/
INSERT INTO leaveRequestTypes (id, name)
VALUES
    (0, 'Annual Leave'),
    (1, 'Study Leave'),
    (2, 'Request Not To Be On Call'),
    --Gap is to allow for any additions, as other should logically come at the end
    (9, 'Other');

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