/*This data is completely made up*/

/*rotaGroups data*/
INSERT INTO rotaGroups (startDate, endDate, status)
VALUES
    ('2021-08-04', '2021-11-02', true);

/*accounts data*/
INSERT INTO accounts (username, email, phone, doctorId, annualLeave, studyLeave, workingHours, accountStatus, doctorStatus, level, rotaGroupId, timeWorked, fixedWorking, password)
VALUES
    ('John Smith', 'test@test.com', '07777 777 777', '9876', 15, 15, 48, 0, 0, 1, 1, 1, false, 'ac3e4d739c0e5c92f38e3999e5ea3ea7c8df08a5590f171154c32259022811cdcb13f5bea6bef6e4');
INSERT INTO accounts (username, email, annualLeave, studyLeave, workingHours, level, password)
VALUES
    ('Jane Smith', 'test2@test.com', 15, 15, 48, 0, 'a1b55606485e2e254d58d0e78a21b42c04d8ced9dfd7048585fb91070dfcd02ac4f3b6e5d41ef4e8'),
    ('User One', 'user1@test.com', 11, 1, 48, 1, '43df9046cc3a4fb668ae54737fdbf328d8c9511a747ad8668f51801896dc4b8881a3b469b3ba0888'),
    ('User Two', 'user2@test.com', 12, 15, 48, 0, 'faa24fee3057db855b3ae36a9f6e61d7dd158186f0c663f1ead28416b6af3d6ae81d43b94c809aaa'),
    ('User Three', 'user3@test.com', 13, 15, 48, 0, 'f381fa5aa4aacb781b1c57c45d887415862bd5cc4e4a6c59c977c27131fd87548a026a5c22b410ce'),
    ('User Four', 'user4@test.com', 14, 15, 48, 0, '59da58a765537a13ba6c27d6c3bfd52bfb5408af3731c5c90221afdfb74567974930b23ae48347af'),
    ('User Five', 'user5@test.com', 15, 15, 47, 0, '4c28bdc548fa46318587668cfb8cc355b60337536ad45e172d0a54b2dd7d3e088627665d904fd450'),
    ('User Six', 'user6@test.com', 16, 15, 48, 0, 'aee9ded41afd440f4d5493f553dac68c9e5fbf334ca9f1bcc1af43ec8f3e94bf0b845729a88d079b'),
    ('User Seven', 'user7@test.com', 17, 15, 48, 0, '9b89fb8cfbeaa96768c8f9bffb0386e29e727b3b37d3839cf5a6481652095ae1bb0e8351126736e8'),
    ('User Eight', 'user8@test.com', 18, 10, 48, 0, '997deee7f17a7b123d2a8479274e4c110ae03a14e365b37fb5f6b662e64b782ad3267e10ad653871'),
    ('User Nine', 'user9@test.com', 19, 15, 48, 0, 'f05254de9080473b9efd00b13966b7f1e42f1846df85ebcc4022b7354773f458ea76488d808615ce'),
    ('User Ten', 'user10@test.com', 20, 15, 48, 0, '7e729f1d898c438f9398d7a2fa66b7c4f4e0e841831603ba3776ddc3f2b47ce984d1746b215a4451');

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