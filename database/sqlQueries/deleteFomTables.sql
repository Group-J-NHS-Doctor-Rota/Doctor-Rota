/*Delete all data from tables in an order so as to not delete source of foreign keys*/

DELETE FROM partTimeDetails WHERE 1=1;

DELETE FROM fixedRotaShifts WHERE 1=1;

DELETE FROM accountRotaTypes WHERE 1=1;

DELETE FROM accountLeaveRequestRelationships WHERE 1=1;

DELETE FROM leaveRequests WHERE 1=1;

DELETE FROM statusTypes WHERE 1=1;

DELETE FROM leaveRequestTypes WHERE 1=1;

DELETE FROM shifts WHERE 1=1;

DELETE FROM shiftTypes WHERE 1=1;

DELETE FROM rotaTypes WHERE 1=1;

DELETE FROM accounts WHERE 1=1;

DELETE FROM rotaGroups WHERE 1=1;