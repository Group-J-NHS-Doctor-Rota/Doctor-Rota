package edu.uob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class PutOperationsTests {

    @Test
    void testPutWorkingDays() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id 999999999 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(999999999, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (999999999, 'Test McTester', 'ndsjkfgndsfpgn', '4567', 'mctester@test.com', 15, 15, 48, 0);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(999999999, c));
            // Try to put working days for an account id which doesn't exist (1000000000)
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertThrows(ResponseStatusException.class, ()-> PutOperations.putWorkingDays(1000000000, true,
                    true, true, false, false, false, false));
            // Try to put working days for new account 999999999
            PutOperations.putWorkingDays(999999999, true, true, true, false, false, false, false);
            // Check row exists and values are correct
            SQL = "SELECT accountId, monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM partTimeDetails WHERE accountId = 999999999;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(999999999, r.getInt("accountid"));
                assertTrue(r.getBoolean("monday"));
                assertTrue(r.getBoolean("tuesday"));
                assertTrue(r.getBoolean("wednesday"));
                assertFalse(r.getBoolean("thursday"));
                assertFalse(r.getBoolean("friday"));
                assertFalse(r.getBoolean("saturday"));
                assertFalse(r.getBoolean("sunday"));

            }
            // Try to put working days for same id
            PutOperations.putWorkingDays(999999999, true, false, false, true, true, false, false);
            // Check values have been updated
            SQL = "SELECT accountId, monday, tuesday, wednesday, thursday, friday, saturday, sunday FROM partTimeDetails WHERE accountId = 999999999;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(999999999, r.getInt("accountId"));
                assertTrue(r.getBoolean("monday"));
                assertFalse(r.getBoolean("tuesday"));
                assertFalse(r.getBoolean("wednesday"));
                assertTrue(r.getBoolean("thursday"));
                assertTrue(r.getBoolean("friday"));
                assertFalse(r.getBoolean("saturday"));
                assertFalse(r.getBoolean("sunday"));

            }
            // Delete all test data
            SQL = "DELETE FROM partTimeDetails WHERE accountId = 999999999; " +
                    "DELETE FROM accounts WHERE id = 999999999;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(999999999, c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    public void testPutFixedShift() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            int id = 999999070;
            Date date1 = Date.valueOf("1922-07-18");

            // Create new account with id 999999070 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (999999070, '070 Test User', 'pwd999999070', '9070', 'test_user070@test.com', 15, 15, 48, 0);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id, c));

            // Try to put fixed rota shifts for an account id which doesn't exist (1000000001)
            assertFalse(ConnectionTools.accountIdExists(1000000001, c));
            assertThrows(ResponseStatusException.class,
                    ()-> PutOperations.putFixedShift(1000000001, date1, 1));

            // Try to put fixed rota shifts for new account 999999070
            PutOperations.putFixedShift(id, date1, 1);
            // Check row exists and values are correct
            SQL = "SELECT accountId, date, shiftType FROM fixedRotaShifts WHERE accountId = 999999070; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id, r.getInt("accountId"));
                assertEquals(date1, r.getDate("date"));
                assertEquals(1, r.getInt("shiftType"));
            }

            // Try to put fixed shift for same id
            Date date2 = Date.valueOf("1922-07-19");
            PutOperations.putFixedShift(id, date2,2);
            // Check values have been updated
            SQL = "SELECT accountId, date, shiftType FROM fixedRotaShifts WHERE accountId = 999999070;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id, r.getInt("accountId"));
                assertEquals(date2, r.getDate("date"));
                assertEquals(2, r.getInt("shiftType"));
            }
            
            // Delete all test data
            SQL = "DELETE FROM fixedRotaShifts WHERE accountId = 999999070; " +
                    "DELETE FROM accounts WHERE id = 999999070;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(id, c));
            assertFalse(ConnectionTools.idExistInTable(id,
                    "accountId", "fixedRotaShifts", c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }
}
