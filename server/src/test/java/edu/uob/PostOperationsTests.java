package edu.uob;

import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PostOperationsTests {

    @Test
    void testPostLeaveRequest() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id 999999999 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(929929929, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (929929929, 'Testy Tim', 'dfjghkjhk', 's6w7ju', 'tim@test.com', 15, 15, 48, 0);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(929929929, c));
            // Try to post a leave request for an account id which doesn't exist (1000000000)
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertThrows(ResponseStatusException.class, ()-> PostOperations.postRequestLeave(1000000000,
                    "2020-02-02", 0, 0, "Note here"));
            // Try to post a leave request for new account 929929929
            PostOperations.postRequestLeave(929929929, "2020-02-02", 0, 0, "Note here");
            // Check row exists and values are correct
            SQL = "SELECT * FROM leaveRequests WHERE accountId = 929929929;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(929929929, r.getInt("accountId"));
                assertEquals("2020-02-02", r.getString("date"));
                assertEquals(0, r.getInt("type"));
                assertEquals(0, r.getInt("length"));
                assertEquals("Note here", r.getString("note"));

            }
            // Check notification was created
            SQL = "SELECT EXISTS ( SELECT n.id FROM notifications n " +
                    "LEFT OUTER JOIN leaverequests l on n.detailid = l.id " +
                    "WHERE n.type = 0 AND l.accountId = 929929929 AND l.date = cast('2020-02-02' AS date) ); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertTrue(r.getBoolean(1));
            }
            // Try to post a leave request for same id and date
            PostOperations.postRequestLeave(929929929, "2020-02-02", 2, 1, "Comments");
            SQL = "SELECT * FROM leaveRequests WHERE accountId = 929929929;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(929929929, r.getInt("accountId"));
                assertEquals("2020-02-02", r.getString("date"));
                assertEquals(2, r.getInt("type"));
                assertEquals(1, r.getInt("length"));
                assertEquals("Comments", r.getString("note"));

            }
            // Delete all test data
            DeleteOperations.deleteAccount(929929929);
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(929929929, c));
        } catch (Exception e) {
            DeleteOperations.deleteAccount(929929929);
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

}