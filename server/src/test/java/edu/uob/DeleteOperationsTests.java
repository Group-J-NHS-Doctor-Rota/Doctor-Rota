package edu.uob;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class DeleteOperationsTests {

    @Test
    void testDeleteAccount() {
        // Get random account id, username and email to test
        int id1 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String email1 = TestTools.getRandomEmail();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id1
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, email) " +
                    "VALUES (?, ?, 'sdfdfsgghndfh', ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, username1);
                s.setString(3, email1);
                s.executeUpdate();
            }
            // Add data to various tables
            PutOperations.putWorkingDays(id1, true, true, true, true, false, false, false);
            PutOperations.putFixedShift(id1, Date.valueOf("2022-07-13"), 1);
            PostOperations.postRequestLeave(id1, "2022-07-12", 0, 0, "");
            SQL = "INSERT INTO shifts (accountId, rotaGroupId, rotaTypeId, date, type, ruleNotes) VALUES (?, 1, 1, '2022-07-11', 0, ''); " +
                    "INSERT INTO accountLeaveRequestRelationships (accountId, leaveRequestId, status) VALUES (?, 1, 1); " +
                    "INSERT INTO accountRotaTypes (accountId, rotaTypeId, startDate, endDate) VALUES (?, 2, '2022-07-01', '2022-08-01'); " +
                    "INSERT INTO tokens (accountId, token) VALUES (?, 'sdgdsfgtu348090j9jgkdslfg');";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setInt(2, id1);
                s.setInt(3, id1);
                s.setInt(4, id1);
                s.executeUpdate();
            }
            // Check data has gone in
            assertTrue(ConnectionTools.idExistInTable(id1, "accountId", "partTimeDetails", c));
            assertTrue(ConnectionTools.idExistInTable(id1, "accountId", "fixedRotaShifts", c));
            assertTrue(ConnectionTools.idExistInTable(id1, "accountId", "accountRotaTypes", c));
            assertTrue(ConnectionTools.idExistInTable(id1, "accountId", "accountLeaveRequestRelationships", c));
            assertTrue(ConnectionTools.idExistInTable(id1, "accountId", "leaveRequests", c));
            assertTrue(ConnectionTools.idExistInTable(id1, "accountId", "shifts", c));
            assertTrue(ConnectionTools.idExistInTable(id1, "accountId", "tokens", c));
            assertTrue(ConnectionTools.idExistInTable(id1, "id", "accounts", c));
            // Check notification id
            SQL = "SELECT EXISTS (SELECT n.* FROM notifications n " +
                    "LEFT JOIN leaverequests l on n.detailid = l.id WHERE n.type = 0 " +
                    "AND l.accountId = ? AND l.date = cast(? AS date));";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, "2022-07-12");
                ResultSet r = s.executeQuery();
                r.next();
                assertTrue(r.getBoolean(1));
            }
            // Delete account
            DeleteOperations.deleteAccount(id1);
            // Try to delete account again
            assertThrows(ResponseStatusException.class, ()-> DeleteOperations.deleteAccount(id1));
            // Check data is no longer there
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "partTimeDetails", c));
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "fixedRotaShifts", c));
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "accountRotaTypes", c));
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "accountLeaveRequestRelationships", c));
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "leaveRequests", c));
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "shifts", c));
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "tokens", c));
            assertFalse(ConnectionTools.idExistInTable(id1, "id", "accounts", c));
            // Check notification id
            SQL = "SELECT EXISTS (SELECT n.* FROM notifications n " +
                    "LEFT JOIN leaverequests l on n.detailid = l.id WHERE n.type = 0 " +
                    "AND l.accountId = ? AND l.date = cast(? AS date));";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, "2022-07-12");
                ResultSet r = s.executeQuery();
                r.next();
                assertFalse(r.getBoolean(1));
            }
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

}
