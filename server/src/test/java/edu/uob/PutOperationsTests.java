package edu.uob;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class PutOperationsTests {

    @Test
    void testPutWorkingDays() {
        // Get random id to test
        int id1 = TestTools.getTestAccountId();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (?, 'Test McTester', 'ndsjkfgndsfpgn', '98765', 'mctester@test.com', 15, 15, 48, 0);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));
            // Try to put working days for an account id which doesn't exist (1000000000)
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertThrows(ResponseStatusException.class, ()-> PutOperations.putWorkingDays(1000000000, true,
                    true, true, false, false, false, false));
            // Try to put working days for new account 999999999
            PutOperations.putWorkingDays(id1, true, true, true, false, false, false, false);
            // Check row exists and values are correct
            SQL = "SELECT accountId, monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM partTimeDetails WHERE accountId = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id1, r.getInt("accountid"));
                assertTrue(r.getBoolean("monday"));
                assertTrue(r.getBoolean("tuesday"));
                assertTrue(r.getBoolean("wednesday"));
                assertFalse(r.getBoolean("thursday"));
                assertFalse(r.getBoolean("friday"));
                assertFalse(r.getBoolean("saturday"));
                assertFalse(r.getBoolean("sunday"));

            }
            // Try to put working days for same id
            PutOperations.putWorkingDays(id1, true, false, false, true, true, false, false);
            // Check values have been updated
            SQL = "SELECT accountId, monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM partTimeDetails WHERE accountId = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id1, r.getInt("accountId"));
                assertTrue(r.getBoolean("monday"));
                assertFalse(r.getBoolean("tuesday"));
                assertFalse(r.getBoolean("wednesday"));
                assertTrue(r.getBoolean("thursday"));
                assertTrue(r.getBoolean("friday"));
                assertFalse(r.getBoolean("saturday"));
                assertFalse(r.getBoolean("sunday"));

            }
            // Delete all test data
            DeleteOperations.deleteAccount(id1);
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(id1, c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    public void testPutFixedShift() {
        // Get random id to test
        int id1 = TestTools.getTestAccountId();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            Date date1 = Date.valueOf("1922-07-18");
            // Create new account with id (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (?, '070 Test User', 'pwd999999070', '9070', 'test_user070@test.com', 15, 15, 48, 0);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));

            // Try to put fixed rota shifts for an account id which doesn't exist (1000000000)
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertThrows(ResponseStatusException.class,
                    ()-> PutOperations.putFixedShift(1000000000, date1, 1));

            // Try to put fixed rota shifts for new account
            PutOperations.putFixedShift(id1, date1, 1);
            // Check row exists and values are correct
            SQL = "SELECT accountId, date, shiftType FROM fixedRotaShifts WHERE accountId = ?; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id1, r.getInt("accountId"));
                assertEquals(date1, r.getDate("date"));
                assertEquals(1, r.getInt("shiftType"));
            }

            // Try to put fixed shift for same id
            Date date2 = Date.valueOf("1922-07-19");
            PutOperations.putFixedShift(id1, date2,2);
            // Check values have been updated
            SQL = "SELECT accountId, date, shiftType FROM fixedRotaShifts WHERE accountId = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id1, r.getInt("accountId"));
                assertEquals(date2, r.getDate("date"));
                assertEquals(2, r.getInt("shiftType"));
            }
            
            // Delete all test data
            DeleteOperations.deleteAccount(id1);
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.idExistInTable(id1,
                    "accountId", "fixedRotaShifts", c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }
}
