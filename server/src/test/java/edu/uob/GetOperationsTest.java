package edu.uob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetOperationsTest {

    @Test
    void testGetNotifications() {

        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new accounts with ids 999999199 and 999999299 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(999999199, c));
            assertFalse(ConnectionTools.accountIdExists(999999299, c));
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (999999199, 'Test McTester', 'ndsjkfgndsfpgn', '45678', 'mctester@test.com', 15, 15, 48, 0), " +
                    "(999999299, 'Testie McTest', 'sdfgsdfgdfs', '45689', 'mctest@test.com', 15, 15, 48, 1);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(999999199, c));
            assertTrue(ConnectionTools.accountIdExists(999999299, c));

            // Add test data for leave requests and notifications
            SQL = "INSERT INTO leaveRequests (id, accountId, date, type, note, status) " +
                    "VALUES (999999991, 999999199, '2021-09-07', 0, '', 0), (999999992, 999999299, '2021-09-08', 1, 'Comment here', 1); " +
                    "INSERT INTO notifications (type, detailId) " +
                    "VALUES (0, 999999991), (0, 999999992);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }

            // Check response expecting all notifications (level 1 account 999999299)
            ResponseEntity<ObjectNode> response = GetOperations.getNotifications(999999299);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertTrue(rootNode.get("leaveRequests").size() >= 2); // Might be greater than 2 as data may already be in table
            assertTrue(response.getBody().toString().contains("leaveRequestId\":999999991,\"accountId\":999999199,\"date\":\"2021-09-07\",\"type\":0,\"note\":\"\",\"status\":0}"));
            assertTrue(response.getBody().toString().contains("leaveRequestId\":999999992,\"accountId\":999999299,\"date\":\"2021-09-08\",\"type\":1,\"note\":\"Comment here\",\"status\":1}"));
            // Check response expecting one notification
            response = GetOperations.getNotifications(999999199);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(1, rootNode.get("leaveRequests").size());
            assertTrue(response.getBody().toString().contains("leaveRequestId\":999999991,\"accountId\":999999199,\"date\":\"2021-09-07\",\"type\":0,\"note\":\"\",\"status\":0}"));
            // Check response expecting no notifications (no account)
            response = GetOperations.getNotifications(1000000000);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(0, rootNode.get("leaveRequests").size());
            assertTrue(response.getBody().toString().contains("{\"leaveRequests\":[]"));

            // Delete all test data
            DeleteOperations.deleteAccount(999999199);
            DeleteOperations.deleteAccount(999999299);
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(999999199, c));
            assertFalse(ConnectionTools.accountIdExists(999999299, c));
        } catch (Exception e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testGetLeaves() {
        int id1 = 999999075;
        int id2 = 999999076;
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new accounts with ids 999999075 and 999999076 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.accountIdExists(id2, c));
            assertFalse(ConnectionTools.accountIdExists(1000000330, c));
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "leaveRequests", c));
            assertFalse(ConnectionTools.idExistInTable(id2, "accountId", "leaveRequests", c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (999999075, 'testuser3', 'pwd999999075', '9075', 'user3@test.com', 75, 705, 48, 0), " +
                    "(999999076, 'testuser4', 'pwd999999076', '9076', 'user4@test.com', 76, 706, 48, 1);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));
            assertTrue(ConnectionTools.accountIdExists(id2, c));

            // Create several leave requests
            SQL = "INSERT INTO leaveRequests (accountId, date, type, length, note, status) " +
                    "VALUES (999999075, '2022-01-01', 0, 0, 'annual leave full day', 1), " +
                    "(999999075, '2022-01-02', 0, 1, 'annual leave am', 1), " +
                    "(999999075, '2022-01-03', 0, 2, 'annual leave pm rejected', 2), " +
                    "(999999076, '2022-02-03', 1, 2, 'study leave pm', 1), " +
                    "(999999076, '2022-02-27', 0, 0, 'annual leave full day pending', 0); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }

            // Check response:
            // Check response (level 1 account 999999076)
            ResponseEntity<ObjectNode> response = GetOperations.getLeaves(id2);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(1, rootNode.get("leaves").size());
            assertTrue(response.getBody().toString().contains(
                    "{\"studyLeave\":705.5,\"annualLeave\":76.0}"
            ));
            // Check response (level 0 account 999999075)
            response = GetOperations.getLeaves(id1);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(1, rootNode.get("leaves").size());
            assertTrue(response.getBody().toString().contains(
                    "{\"studyLeave\":705.0,\"annualLeave\":73.5}"
            ));

            // Check response expecting no shifts (no account)
            response = GetOperations.getLeaves(1000000330);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(0, rootNode.get("leaves").size());
            assertTrue(response.getBody().toString().contains("{\"leaves\":[]"));

            // Delete all test data
            DeleteOperations.deleteAccount(id1);
            DeleteOperations.deleteAccount(id2);
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.accountIdExists(id2, c));
        } catch (Exception e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testGetAccount() {
        // Get first accountId (should always be at least one account)
        int accountId = 0;
        boolean foundId = false;
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            while(!foundId) {
                accountId++;
                foundId = ConnectionTools.accountIdExists(accountId, c);
                // Cap search at 1000 to avoid test running on too long
                if(accountId >= 1000) {
                    return;
                }
            }
            ResponseEntity<ObjectNode> response = GetOperations.getAccount(accountId);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            // Check response has all the required fields
            assertTrue(rootNode.has("id"));
            assertTrue(rootNode.has("username"));
            assertTrue(rootNode.has("email"));
            assertTrue(rootNode.has("phone"));
            assertTrue(rootNode.has("doctorId"));
            assertTrue(rootNode.has("annualLeave"));
            assertTrue(rootNode.has("studyLeave"));
            assertTrue(rootNode.has("workingHours"));
            assertTrue(rootNode.has("accountStatus"));
            assertTrue(rootNode.has("doctorStatus"));
            assertTrue(rootNode.has("level"));
            assertTrue(rootNode.has("timeWorked"));
            assertTrue(rootNode.has("fixedWorking"));
            // Check the id field aligns
            assertEquals(accountId, rootNode.get("id").asInt());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testGetMissingAccount() {
        // A missing account will return an error response
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            assertFalse(ConnectionTools.accountIdExists(919919919, c));
        } catch (SQLException e) {
                fail(e);
        }
        assertThrows(ResponseStatusException.class, ()-> GetOperations.getAccount(919919919));
    }

    @Test
    void testGetAllAccounts() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            ResponseEntity<ObjectNode> response = GetOperations.getAllAccounts();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            int numberOfAccounts = rootNode.get("accounts").size();
            // If there are no accounts, then empty list
            if(numberOfAccounts <= 0) {
                return;
            }
            // Check all accounts, have all the fields
            for(int i = 0; i < numberOfAccounts; i++) {
                JsonNode account = rootNode.get("accounts").get(0);
                assertTrue(account.has("id"));
                assertTrue(account.has("username"));
                assertTrue(account.has("email"));
                assertTrue(account.has("phone"));
                assertTrue(account.has("doctorId"));
                assertTrue(account.has("annualLeave"));
                assertTrue(account.has("studyLeave"));
                assertTrue(account.has("workingHours"));
                assertTrue(account.has("accountStatus"));
                assertTrue(account.has("doctorStatus"));
                assertTrue(account.has("level"));
                assertTrue(account.has("timeWorked"));
                assertTrue(account.has("fixedWorking"));
            }
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testGetShifts() {
        int id1 = 999999001;
        int id2 = 999999077;
        int year = 1922;
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new accounts with ids 999999001 and 999999077 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.accountIdExists(id2, c));
            assertFalse(ConnectionTools.accountIdExists(999999333, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (999999001, 'testuser1', 'pwd999999001', '4567', 'user1@test.com', 15, 15, 48, 0), " +
                    "(999999077, 'testuser2', 'pwd999999077', '4568', 'user2@test.com', 15, 15, 48, 1);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));
            assertTrue(ConnectionTools.accountIdExists(id2, c));
            // Add test data in table 'shifts'
            SQL = "INSERT INTO shifts (id, accountId, rotaGroupId, rotaTypeId, date, type, ruleNotes) " +
                    "VALUES (999999901, 999999001, 1, 4, '1922-09-01'::date, 0, 'rule'), " +
                    "(999999977, 999999077, 1, 4, '1922-09-07'::date, 1, 'note'); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check response:
            // level 1 account 999999077 will get all the shift data from table 'shift'
            ResponseEntity<ObjectNode> response = GetOperations.getShifts(year, id2);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertTrue(rootNode.get("shifts").size() >= 2);// Might be greater than 2 as data may already be in table
            assertTrue(response.getBody().toString().contains(
                    "{\"id\":999999901,\"accountId\":999999001,\"username\":\"testuser1\",\"rotaType\":4,\"date\":\"1922-09-01\",\"type\":0,\"ruleNotes\":\"rule\",\"accountLevel\":0}"
            ));
            assertTrue(response.getBody().toString().contains(
                    "{\"id\":999999977,\"accountId\":999999077,\"username\":\"testuser2\",\"rotaType\":4,\"date\":\"1922-09-07\",\"type\":1,\"ruleNotes\":\"note\",\"accountLevel\":1}"
            ));
            // Check response expecting one shift (level 0 account)
            response = GetOperations.getShifts(year, id1);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(1, rootNode.get("shifts").size());
            assertTrue(response.getBody().toString().contains(
                    "{\"id\":999999901,\"accountId\":999999001,\"username\":\"testuser1\",\"rotaType\":4,\"date\":\"1922-09-01\",\"type\":0,\"ruleNotes\":\"rule\",\"accountLevel\":0}"
            ));
            // Check response expecting no shifts (no account)
            response = GetOperations.getShifts(year, 999999333);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(0, rootNode.get("shifts").size());
            assertTrue(response.getBody().toString().contains("{\"shifts\":[]}"));
            // Delete all test data
            DeleteOperations.deleteAccount(id1);
            DeleteOperations.deleteAccount(id2);
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.accountIdExists(id2, c));
        } catch (Exception e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }
}
