package edu.uob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatchOperationsTests {

    @Test
    void testUpdateVariable() {
        // Get random account id, username and email to test
        int id1 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String email1 = TestTools.getRandomEmail();
        String email2 = TestTools.getRandomEmail();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with random account id (definitely unused) and some data
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, email) " +
                    "VALUES (?, ?, 'sdfdfsgghndfh', ?); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, username1);
                s.setString(3, email1);
                s.executeUpdate();
            }
            // Change some data (3 bits)
            PatchOperations.updateVariable(email2, "string", "email", "accounts", id1, c);
            PatchOperations.updateVariable("true", "boolean", "fixedWorking", "accounts", id1, c);
            PatchOperations.updateVariable("25", "int", "annualLeave", "accounts", id1, c);
            // Confirm update has worked
            SQL = "SELECT * FROM accounts WHERE id = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(email2, r.getString("email"));
                assertTrue(r.getBoolean("fixedWorking"));
                assertEquals(25, r.getInt("annualLeave"));
            }
            // Delete account
            DeleteOperations.deleteAccount(id1);
            assertFalse(ConnectionTools.accountIdExists(id1, c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testPatchAccount() {
        // Get random account id, username and email to test
        int id1 = TestTools.getTestAccountId();
        String email1 = TestTools.getRandomEmail();
        String realEmail = TestTools.getRandomEmail();
        String connectionString = ConnectionTools.getConnectionString();
        String username1 = TestTools.getRandomUsername();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with random account id (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, email) " +
                    "VALUES (?, ?, 'sdfdsh', ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, username1);
                s.setString(3, email1);
                s.executeUpdate();
            }
            //Update details
            PatchOperations.patchAccount(id1, "30", "29", "20", "1", realEmail,
                    "+447777777777", "123456789", "0", "0", "0.8", "false", "true");
            //Check details
            SQL = "SELECT email, phone, doctorId, annualLeave, studyLeave, workingHours, accountStatus, doctorStatus, level, "+
                    "timeWorked, fixedWorking, painWeek FROM accounts WHERE id = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(realEmail, r.getString("email"));
                assertEquals("+447777777777", r.getString("phone"));
                assertEquals("123456789", r.getString("doctorId"));
                assertEquals(30, r.getInt("annualLeave"));
                assertEquals(29, r.getInt("studyLeave"));
                assertEquals(20, r.getInt("workingHours"));
                assertEquals(0, r.getInt("accountStatus"));
                assertEquals(0, r.getInt("doctorStatus"));
                assertEquals(1, r.getInt("level"));
                assertEquals(0, Float.compare(0.8f, r.getFloat("timeWorked")));
                assertFalse(r.getBoolean("fixedWorking"));
                assertTrue(r.getBoolean("painWeek"));
            }
            //Update only some details
            PatchOperations.patchAccount(id1, null, "39", null, "0", null,
                    "mob: 07777 777777", null, null, null, "0.6", null, "false");
            //Check details
            SQL = "SELECT email, phone, doctorId, annualLeave, studyLeave, workingHours, accountStatus, doctorStatus, level, "+
                    "timeWorked, fixedWorking, painWeek FROM accounts WHERE id = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(realEmail, r.getString("email"));
                assertEquals("mob: 07777 777777", r.getString("phone"));
                assertEquals("123456789", r.getString("doctorId"));
                assertEquals(30, r.getInt("annualLeave"));
                assertEquals(39, r.getInt("studyLeave"));
                assertEquals(20, r.getInt("workingHours"));
                assertEquals(0, r.getInt("accountStatus"));
                assertEquals(0, r.getInt("doctorStatus"));
                assertEquals(0, r.getInt("level"));
                assertEquals(0, Float.compare(0.6f, r.getFloat("timeWorked")));
                assertFalse(r.getBoolean("fixedWorking"));
                assertFalse(r.getBoolean("painWeek"));
            }
            //Update non-existent account
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertThrows(ResponseStatusException.class, ()-> PatchOperations.patchAccount(1000000000, "30", "29", "20", "1", realEmail,
                    "+447777777777", "123456789", "0", "0", "0.8", "false", "false"));
            // Check incorrect data format
            assertThrows(NumberFormatException.class, ()-> PatchOperations.patchAccount(id1, "thirty", "29", "20", "1", realEmail,
                    "+447777777777", "123456789", "0", "0", "0.8", "false", "false"));
            //Delete account
            DeleteOperations.deleteAccount(id1);
            assertFalse(ConnectionTools.accountIdExists(id1, c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testPatchNotification() {
        // Get random ids, username and email to test
        int id1 = TestTools.getTestAccountId(); // as account id
        int id2 = TestTools.getTestAccountId(); // as leave request id
        int id3 = TestTools.getTestAccountId(); // as notification id
        String username1 = TestTools.getRandomUsername();
        String email1 = TestTools.getRandomEmail();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // check the test data
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.idExistInTable(
                    id2, "id", "leaveRequests", c));
            assertFalse(ConnectionTools.idExistInTable(
                    id3, "id", "notifications", c));
            // Create new account with random test id
            String SQL = "INSERT INTO accounts (id, username, password, email) " +
                    "VALUES (?, ?, 'pwd999999073', ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, username1);
                s.setString(3, email1);
                s.executeUpdate();
            }
            // Add test data for leave requests and notifications
            SQL = "INSERT INTO leaveRequests (id, accountId, date, type, note, status) " +
                    "VALUES (?, ?, '1922-07-19', 0, 'note', 0); " +
                    "INSERT INTO notifications (id, type, detailId) " +
                    "VALUES (?, 0, ?); ";
            // note: The type in table 'notification' can only be 0 currently.
            // There is a foreign key constraint with table "notificationTypes"
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id2); s.setInt(2, id1);
                s.setInt(3, id3); s.setInt(4, id2);
                s.executeUpdate();
            }
            // Update notification details
            PatchOperations.patchNotification(id3, id1, "1");
            // Check details
            SQL = "SELECT N.id, N.type, N.detailId, L.id AS leaveRequestId, L.accountId, L.status " +
                    "FROM notifications N " +
                    "LEFT JOIN leaveRequests L on N.detailId = L.id " +
                    "WHERE L.accountId = ?";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id3, r.getInt("id"));
                assertEquals(0, r.getInt("type"));
                assertEquals(id2, r.getInt("detailId"));
                assertEquals(id2, r.getInt("leaveRequestId"));
                assertEquals(id1, r.getInt("accountId"));
                assertEquals(1, r.getInt("status"));
            }
            // check a new record in account leave request relationships
            SQL = "SELECT accountId, status FROM accountLeaveRequestRelationships " +
                    "WHERE leaveRequestId = ?; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id2);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id1, r.getInt("accountId"));
                assertEquals(1, r.getInt("status"));
            }
            // Update non-existent account
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertThrows(ResponseStatusException.class,
                    ()-> PatchOperations.patchNotification(
                            id3, 1000000000, "2"));
            // Delete account
            DeleteOperations.deleteAccount(id1);
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.idExistInTable(
                    id2, "id", "leaveRequests", c));
            assertFalse(ConnectionTools.idExistInTable(
                    id3, "id", "notifications", c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testPatchPassword() throws JsonProcessingException {
        // Create account
        String username = TestTools.getRandomUsername();
        PostOperations.postAccount(username);
        // Login
        String password1 = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
        String password2 = password1 + "a";
        ResponseEntity<ObjectNode> response = GetOperations.getLogin(username, password1);
        JsonNode rootNode = new ObjectMapper().readTree(String.valueOf(response.getBody()));
        int accountId = rootNode.get("accountId").asInt();
        // Patch password with same password
        assertThrows(ResponseStatusException.class,
                ()-> PatchOperations.patchPassword(password1, password1, accountId));
        // Patch password with different password
        PatchOperations.patchPassword(password1, password2, accountId);
        // Check login again
        assertThrows(ResponseStatusException.class,
                ()-> GetOperations.getLogin(username, password1));
        GetOperations.getLogin(username, password2);
        // Delete account
        DeleteOperations.deleteAccount(accountId);
    }

    @Test
    void testPatchPasswordReset() throws JsonProcessingException {
        // Create account with random username and email
        String username = TestTools.getRandomUsername();
        String email = TestTools.getRandomEmail();
        PostOperations.postAccount(username, email);
        // Login
        String password1 = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
        String password2 = password1 + "a";
        ResponseEntity<ObjectNode> response = GetOperations.getLogin(username, password1);
        JsonNode rootNode = new ObjectMapper().readTree(String.valueOf(response.getBody()));
        int accountId = rootNode.get("accountId").asInt();
        // Patch password with different password
        PatchOperations.patchPassword(password1, password2, accountId);
        // Check login again
        assertThrows(ResponseStatusException.class,
                ()-> GetOperations.getLogin(username, password1));
        GetOperations.getLogin(username, password2);
        // Reset password
        PatchOperations.patchPasswordReset(username, email);
        // Check login again
        assertThrows(ResponseStatusException.class,
                ()-> GetOperations.getLogin(username, password2));
        GetOperations.getLogin(username, password1);
        // Delete account
        DeleteOperations.deleteAccount(accountId);
    }

    @Test
    void testPatchLogout() throws JsonProcessingException {
        // Create account
        String username = TestTools.getRandomUsername();
        PostOperations.postAccount(username);
        // Login
        String password1 = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
        ResponseEntity<ObjectNode> response = GetOperations.getLogin(username, password1);
        JsonNode rootNode = new ObjectMapper().readTree(String.valueOf(response.getBody()));
        int accountId = rootNode.get("accountId").asInt();
        String token1 = rootNode.get("token").asText();
        // Logout
        PatchOperations.patchLogout(token1);
        // Login
        response = GetOperations.getLogin(username, password1);
        rootNode = new ObjectMapper().readTree(String.valueOf(response.getBody()));
        String token2 = rootNode.get("token").asText();
        // Compare tokens
        assertNotEquals(token1, token2, "Token should have changed after logout");
        // Delete account
        DeleteOperations.deleteAccount(accountId);
    }
}
