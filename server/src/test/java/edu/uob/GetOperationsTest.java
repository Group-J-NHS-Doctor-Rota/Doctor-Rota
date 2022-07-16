package edu.uob;

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
                    "VALUES (999999199, 'Test McTester', 'ndsjkfgndsfpgn', '4567', 'mctester@test.com', 15, 15, 48, 0), " +
                    "(999999299, 'Testie McTest', 'sdfgsdfgdfs', '4568', 'mctest@test.com', 15, 15, 48, 1);";
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
            SQL = "DELETE FROM notifications WHERE detailId IN (SELECT detailId FROM notifications n " +
                    "LEFT JOIN leaveRequests l ON n.detailId = l.id WHERE accountId IN (999999199,999999299)) AND type = 0; " +
                    "DELETE FROM leaveRequests WHERE accountId IN (999999199,999999299); " +
                    "DELETE FROM accounts WHERE id IN (999999199,999999299);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(999999199, c));
            assertFalse(ConnectionTools.accountIdExists(999999299, c));
        } catch (Exception e) {
            fail("Database connection and SQL queries should have worked\n" + e);
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
            ResponseEntity<ObjectNode> response = GetOperations.getShifts(id2, year);
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
            response = GetOperations.getShifts(id1, year);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(1, rootNode.get("shifts").size());
            assertTrue(response.getBody().toString().contains(
                    "{\"id\":999999901,\"accountId\":999999001,\"username\":\"testuser1\",\"rotaType\":4,\"date\":\"1922-09-01\",\"type\":0,\"ruleNotes\":\"rule\",\"accountLevel\":0}"
            ));
            // Check response expecting no shifts (no account)
            response = GetOperations.getShifts(999999333, year);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(0, rootNode.get("shifts").size());
            assertTrue(response.getBody().toString().contains("{\"shifts\":[]}"));
            // Delete all test data
            SQL = "DELETE FROM shifts WHERE accountId IN (999999001,999999077); " +
                    "DELETE FROM accounts WHERE id IN (999999001,999999077);" +
                    "DELETE FROM rotaTypes WHERE id IN(901, 977); " +
                    "DELETE FROM rotaGroups WHERE id IN(901, 977);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.accountIdExists(id2, c));
        } catch (Exception e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }
}
