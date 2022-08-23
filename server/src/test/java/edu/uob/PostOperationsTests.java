package edu.uob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class PostOperationsTests {

    @Test
    void testPostLeaveRequest() throws SQLException {
        // Get random id, username and email to test
        int id1 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String email1 = TestTools.getRandomEmail();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, email) " +
                    "VALUES (?, ?, 'dfjghkjhk', ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, username1);
                s.setString(3, email1);
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));
            // Try to post a leave request for an account id which doesn't exist (1000000000)
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertThrows(ResponseStatusException.class, ()-> PostOperations.postRequestLeave(1000000000,
                    "2020-02-02", 0, 0, "Note here"));
            // Try to post a leave request for new account 929929929
            PostOperations.postRequestLeave(id1, "2020-02-02", 0, 0, "Note here");
            // Check row exists and values are correct
            SQL = "SELECT * FROM leaveRequests WHERE accountId = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id1, r.getInt("accountId"));
                assertEquals("2020-02-02", r.getString("date"));
                assertEquals(0, r.getInt("type"));
                assertEquals(0, r.getInt("length"));
                assertEquals("Note here", r.getString("note"));

            }
            // Check notification was created
            SQL = "SELECT EXISTS ( SELECT n.id FROM notifications n " +
                    "LEFT OUTER JOIN leaverequests l on n.detailid = l.id " +
                    "WHERE n.type = 0 AND l.accountId = ? AND l.date = cast('2020-02-02' AS date) ); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertTrue(r.getBoolean(1));
            }
            // Try to post a leave request for same id and date
            PostOperations.postRequestLeave(id1, "2020-02-02", 2, 1, "Comments");
            SQL = "SELECT * FROM leaveRequests WHERE accountId = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(id1, r.getInt("accountId"));
                assertEquals("2020-02-02", r.getString("date"));
                assertEquals(2, r.getInt("type"));
                assertEquals(1, r.getInt("length"));
                assertEquals("Comments", r.getString("note"));

            }
            // Delete all test data
            DeleteOperations.deleteAccount(id1);
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(id1, c));
        } catch (Exception e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testPostAccount() {
        // Make new account
        String username = TestTools.getRandomUsername();
        String email = TestTools.getRandomEmail();
        int accountId = 0; //Needs to have value and 0 will always cause exception if not overwritten
        PostOperations.postAccount(username, email);
        // Try to make new account with same username
        assertThrows(ResponseStatusException.class, ()-> PostOperations.postAccount(username, email),
                "Should not be able to create a second account with the same username!");
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Check values
            String SQL = "SELECT * FROM accounts WHERE username = ?; ";
            String hashed_password;
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, username);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(username, r.getString("username"));
                assertEquals(email, r.getString("email"));
                hashed_password = r.getString("password");
                accountId = r.getInt("id");
            }
            SQL = "SELECT EXISTS ( SELECT accountId FROM tokens WHERE accountId = ?); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                ResultSet r = s.executeQuery();
                r.next();
                assertTrue(r.getBoolean(1));
            }
            // Check password
            Encryption encryption = new Encryption();
            String default_password = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
            assertTrue(encryption.passwordMatches(default_password, hashed_password));
        } catch (SQLException e) {
            fail(e.toString());
        }
        // Delete account
        DeleteOperations.deleteAccount(accountId);
    }

    @Test
    void testPostAccountWithoutEmail() {
        // Make new account
        String username = TestTools.getRandomUsername();
        int accountId = 0; //Needs to have value and 0 will always cause exception if not overwritten
        PostOperations.postAccount(username, null);
        // Try to make new account with same username
        assertThrows(ResponseStatusException.class, ()-> PostOperations.postAccount(username, null),
                "Should not be able to create a second account with the same username!");
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Check values
            String SQL = "SELECT * FROM accounts WHERE username = ?; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, username);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(username, r.getString("username"));
                assertNull(r.getString("email"));
                accountId = r.getInt("id");
            }
        } catch (SQLException e) {
            fail(e.toString());
        }
        // Delete account
        DeleteOperations.deleteAccount(accountId);
    }

    @Test
    void testPostRotaGroup() throws JsonProcessingException {
        // Get current rota group details
        ResponseEntity<ObjectNode> response = GetOperations.getRotaGroup();
        JsonNode rotaGroups = new ObjectMapper().readTree(String.valueOf(response.getBody())).get("rotaGroups");
        JsonNode rotaGroup;
        String startDateCurrent = "", endDateCurrent = "";
        for(int i = 0; i < rotaGroups.size(); i++) {
            rotaGroup = rotaGroups.get(i);
            if(rotaGroup.get("status").asBoolean()) {
                startDateCurrent = rotaGroup.get("startDate").asText();
                endDateCurrent = rotaGroup.get("endDate").asText();
            }
        }
        // Post new rota group
        PostOperations.postRotaGroup("1974-01-01", "1974-04-04");
        // Check current rota group
        response = GetOperations.getRotaGroup();
        rotaGroups = new ObjectMapper().readTree(String.valueOf(response.getBody())).get("rotaGroups");
        int testId = 0;
        for(int i = 0; i < rotaGroups.size(); i++) {
            rotaGroup = rotaGroups.get(i);
            if(rotaGroup.get("status").asBoolean()) {
                assertEquals("1974-01-01", rotaGroup.get("startDate").asText());
                assertEquals("1974-04-04", rotaGroup.get("endDate").asText());
                testId = rotaGroup.get("id").asInt();
            }
        }
        // Reset rota group
        PostOperations.postRotaGroup(startDateCurrent, endDateCurrent);
        // Delete all test data
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            String SQL = "DELETE FROM rotaGroups WHERE id = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, testId);
                s.executeUpdate();
            }
        } catch (Exception e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

}
