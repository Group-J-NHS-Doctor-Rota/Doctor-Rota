package edu.uob;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
            fail("Database connection and SQL queries should have worked");
        }
    }
}
