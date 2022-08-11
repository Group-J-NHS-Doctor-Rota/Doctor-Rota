package edu.uob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class GetOperationsTest {

    @Test
    void testGetNotifications() {
        // Get random account ids and usernames to test
        int id1 = TestTools.getTestAccountId();
        int id2 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String username2 = TestTools.getRandomUsername();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new accounts with ids 999999199 and 999999299 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.accountIdExists(id2, c));
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            String SQL = "INSERT INTO accounts (id, username, password, email, level) " +
                    "VALUES (?, ?, 'ndsjkfgndsfpgn', 'mctester@test.com', 0), " +
                    "(?, ?, 'sdfgsdfgdfs', 'mctest@test.com', 1);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1); s.setString(2, username1);
                s.setInt(3, id2); s.setString(4, username2);
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));
            assertTrue(ConnectionTools.accountIdExists(id2, c));

            // Add test data for leave requests and notifications
            SQL = "INSERT INTO leaveRequests (id, accountId, date, type, note, status, length) " +
                    "VALUES (?, ?, '2021-09-07', 0, '', 0, 0), (?, ?, '2021-09-08', 1, 'Comment here', 1, 1); " +
                    "INSERT INTO notifications (type, detailId) " +
                    "VALUES (0, ?), (0, ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1); s.setInt(2, id1);
                s.setInt(3, id2); s.setInt(4, id2);
                s.setInt(5, id1); s.setInt(6, id2);
                s.executeUpdate();
            }

            // Check response expecting all notifications (level 1 account 999999299)
            ResponseEntity<ObjectNode> response = GetOperations.getNotifications(id2);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertTrue(rootNode.get("leaveRequests").size() >= 2); // Might be greater than 2 as data may already be in table
            assertTrue(response.getBody().toString().contains("leaveRequestId\":"+id1));
            assertTrue(response.getBody().toString().contains("leaveRequestId\":"+id2));
            // Check each notification has required fields
            int numberOfNotifications = rootNode.get("leaveRequests").size();
            for(int i = 0; i < numberOfNotifications; i++) {
                JsonNode notification = rootNode.get("leaveRequests").get(i);
                assertTrue(notification.has("id"));
                assertTrue(notification.has("leaveRequestId"));
                assertTrue(notification.has("accountId"));
                assertTrue(notification.has("date"));
                assertTrue(notification.has("type"));
                assertTrue(notification.has("note"));
                assertTrue(notification.has("status"));
                if(notification.get("leaveRequestId").asInt() == id2) {
                    assertEquals(id2, notification.get("accountId").asInt());
                    assertEquals("2021-09-08", notification.get("date").asText());
                    assertEquals(1, notification.get("type").asInt());
                    assertEquals("Comment here", notification.get("note").asText());
                    assertEquals(1, notification.get("status").asInt());
                    assertEquals(1, notification.get("length").asInt());
                }
            }
            // Check response expecting one notification
            response = GetOperations.getNotifications(id1);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(1, rootNode.get("leaveRequests").size());
            JsonNode leaveRequest = rootNode.get("leaveRequests").get(0);
            assertEquals(id1, leaveRequest.get("leaveRequestId").asInt());
            assertEquals(id1, leaveRequest.get("accountId").asInt());
            assertEquals("2021-09-07", leaveRequest.get("date").asText());
            assertEquals(0, leaveRequest.get("type").asInt());
            assertEquals("", leaveRequest.get("note").asText());
            assertEquals(0, leaveRequest.get("status").asInt());
            assertEquals(0, leaveRequest.get("length").asInt());
            // Check response expecting no notifications (no account)
            response = GetOperations.getNotifications(1000000000);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(0, rootNode.get("leaveRequests").size());
            assertTrue(response.getBody().toString().contains("{\"leaveRequests\":[]"));

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
    void testGetLeaves() {
        int id1 = TestTools.getTestAccountId();
        int id2 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String username2 = TestTools.getRandomUsername();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new accounts with random ids (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.accountIdExists(id2, c));
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertFalse(ConnectionTools.idExistInTable(id1, "accountId", "leaveRequests", c));
            assertFalse(ConnectionTools.idExistInTable(id2, "accountId", "leaveRequests", c));
            String SQL = "INSERT INTO accounts (id, username, password, email, level, timeWorked) " +
                    "VALUES (?, ?, 'pwd999999075', 'user3@test.com', 0, 0.8), " +
                    "(?, ?, 'pwd999999076', 'user4@test.com', 1, 1);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1); s.setString(2, username1);
                s.setInt(3, id2); s.setString(4, username2);
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));
            assertTrue(ConnectionTools.accountIdExists(id2, c));

            // Create several leave requests
            SQL = "INSERT INTO leaveRequests (accountId, date, type, length, note, status) " +
                    "VALUES (?, '2022-01-01', 0, 0, 'annual leave full day', 1), " +
                    "(?, '2022-01-02', 0, 1, 'annual leave am', 1), " +
                    "(?, '2022-01-03', 0, 2, 'annual leave pm rejected', 2), " +
                    "(?, '2022-02-03', 1, 2, 'study leave pm', 1), " +
                    "(?, '2022-02-27', 0, 0, 'annual leave full day pending', 0); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1); s.setInt(2, id1); s.setInt(3, id1);
                s.setInt(4, id2); s.setInt(5, id2);
                s.executeUpdate();
            }

            // Check response:
            // Check response for one leaves (level 1 account)
            ResponseEntity<ObjectNode> response = GetOperations.getLeaves(id2);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertTrue(rootNode.has("id"));
            assertTrue(rootNode.has("studyLeave"));
            assertTrue(rootNode.has("annualLeave"));
            assertEquals(id2, rootNode.get("id").asInt());
            assertEquals(14.5, rootNode.get("studyLeave").asDouble());
            assertEquals(30.0, rootNode.get("annualLeave").asDouble());

            // Check response for one leaves (level 0 account)
            response = GetOperations.getLeaves(id1);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertTrue(rootNode.has("id"));
            assertTrue(rootNode.has("studyLeave"));
            assertTrue(rootNode.has("annualLeave"));
            assertEquals(id1, rootNode.get("id").asInt());
            assertEquals(12.0, rootNode.get("studyLeave").asDouble());
            assertEquals(22.5, rootNode.get("annualLeave").asDouble());

            // Check response expecting no shifts (no account)
            response = GetOperations.getLeaves(1000000000);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertTrue(rootNode.isEmpty());
            assertEquals("{}", response.getBody().toString());

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
            // Only check part-time days if non-empty
            assertTrue(rootNode.has("partTimeDetails"));
            JsonNode partTimeDetails = rootNode.get("partTimeDetails");
            if(!partTimeDetails.isEmpty()) {
                assertTrue(partTimeDetails.has("monday"));
                assertTrue(partTimeDetails.has("tuesday"));
                assertTrue(partTimeDetails.has("wednesday"));
                assertTrue(partTimeDetails.has("thursday"));
                assertTrue(partTimeDetails.has("friday"));
                assertTrue(partTimeDetails.has("saturday"));
                assertTrue(partTimeDetails.has("sunday"));
            }
            // Only check actual shifts for fixedRotaShifts if true
            assertTrue(rootNode.has("fixedRotaShifts"));
            JsonNode fixedRotaShifts = rootNode.get("fixedRotaShifts");
            if(rootNode.get("fixedWorking").asBoolean()) {
                for(int j = 0; j < fixedRotaShifts.size(); j++) {
                    JsonNode fixedRotaShift = fixedRotaShifts.get(j);
                    assertTrue(fixedRotaShift.has("id"));
                    assertTrue(fixedRotaShift.has("date"));
                    assertTrue(fixedRotaShift.has("shiftType"));
                }
            } else {
                assertTrue(fixedRotaShifts.isEmpty());
            }
            // Check all account rota types
            assertTrue(rootNode.has("accountRotaTypes"));
            JsonNode accountRotaTypes = rootNode.get("accountRotaTypes");
            for(int j = 0; j < accountRotaTypes.size(); j++) {
                JsonNode accountRotaType = accountRotaTypes.get(j);
                assertTrue(accountRotaType.has("id"));
                assertTrue(accountRotaType.has("rotaTypeId"));
                assertTrue(accountRotaType.has("startDate"));
                assertTrue(accountRotaType.has("endDate"));
            }
            // Check the id field aligns
            assertEquals(accountId, rootNode.get("id").asInt());
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void testGetMissingAccount() {
        // Get a random accountId for a non-existent account
        int id1 = TestTools.getTestAccountId();
        // A missing account will return an error response
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            assertFalse(ConnectionTools.accountIdExists(id1, c));
        } catch (SQLException e) {
                fail(e);
        }
        assertThrows(ResponseStatusException.class, ()-> GetOperations.getAccount(id1));
    }

    @Test
    void testGetAllAccounts() throws JsonProcessingException {
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
            JsonNode account = rootNode.get("accounts").get(i);
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
            // Only check part-time days if non-empty
            assertTrue(account.has("partTimeDetails"));
            JsonNode partTimeDetails = account.get("partTimeDetails");
            if(!partTimeDetails.isEmpty()) {
                assertTrue(partTimeDetails.has("monday"));
                assertTrue(partTimeDetails.has("tuesday"));
                assertTrue(partTimeDetails.has("wednesday"));
                assertTrue(partTimeDetails.has("thursday"));
                assertTrue(partTimeDetails.has("friday"));
                assertTrue(partTimeDetails.has("saturday"));
                assertTrue(partTimeDetails.has("sunday"));
            }
            // Only check actual shifts for fixedRotaShifts if true
            assertTrue(account.has("fixedRotaShifts"));
            JsonNode fixedRotaShifts = account.get("fixedRotaShifts");
            if(account.get("fixedWorking").asBoolean()) {
                for(int j = 0; j < fixedRotaShifts.size(); j++) {
                    JsonNode fixedRotaShift = fixedRotaShifts.get(j);
                    assertTrue(fixedRotaShift.has("id"));
                    assertTrue(fixedRotaShift.has("date"));
                    assertTrue(fixedRotaShift.has("shiftType"));
                }
            } else {
                assertTrue(fixedRotaShifts.isEmpty());
            }
            // Check all account rota types
            assertTrue(account.has("accountRotaTypes"));
            JsonNode accountRotaTypes = account.get("accountRotaTypes");
            for(int j = 0; j < accountRotaTypes.size(); j++) {
                JsonNode accountRotaType = accountRotaTypes.get(j);
                assertTrue(accountRotaType.has("id"));
                assertTrue(accountRotaType.has("rotaTypeId"));
                assertTrue(accountRotaType.has("startDate"));
                assertTrue(accountRotaType.has("endDate"));
            }
        }
    }

    @Test
    void testGetShifts() {
        int id1 = TestTools.getTestAccountId();
        int id2 = TestTools.getTestAccountId();
        int id3 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String username2 = TestTools.getRandomUsername();
        int year = 1922;
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new accounts with random ids (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.accountIdExists(id2, c));
            assertFalse(ConnectionTools.accountIdExists(id3, c));
            String SQL = "INSERT INTO accounts (id, username, password, email, level) " +
                    "VALUES (?, ?, 'pwd999999001', 'user1@test.com', 0), " +
                    "(?, ?, 'pwd999999077', 'user2@test.com', 1);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1); s.setString(2, username1);
                s.setInt(3, id2); s.setString(4, username2);
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));
            assertTrue(ConnectionTools.accountIdExists(id2, c));
            // Add test data in table 'shifts'
            SQL = "INSERT INTO shifts (id, accountId, rotaGroupId, rotaTypeId, date, type, ruleNotes) " +
                    "VALUES (?, ?, 1, 4, '1922-09-01'::date, 0, 'rule'), " +
                    "(?, ?, 1, 4, '1922-09-07'::date, 1, 'note'); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1); s.setInt(2, id1);
                s.setInt(3, id2); s.setInt(4, id2);
                s.executeUpdate();
            }
            // Add test data for leave requests
            PostOperations.postRequestLeave(id1, year+"-09-11", 0, 0, "");
            PostOperations.postRequestLeave(id2, year+"-09-12", 1, 1, "");
            // Check response:
            // level 1 account will get all the shift data and leave requests for the given year
            ResponseEntity<ObjectNode> response = GetOperations.getShifts(year, id2);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertTrue(rootNode.get("shifts").size() >= 2);// Might be greater than 2 as data may already be in table
            assertTrue(rootNode.get("leave").size() >= 2);
            assertTrue(response.getBody().toString().contains(
                    "{\"id\":"+id1+",\"accountId\":"+id1+
                            ",\"username\":\""+username1+"\",\"rotaType\":4,\"date\":\"1922-09-01\",\"type\":0,\"ruleNotes\":\"rule\",\"accountLevel\":0}"
            ));
            assertTrue(response.getBody().toString().contains(
                    "{\"id\":"+id2+",\"accountId\":"+id2+
                            ",\"username\":\""+username2+"\",\"rotaType\":4,\"date\":\"1922-09-07\",\"type\":1,\"ruleNotes\":\"note\",\"accountLevel\":1}"
            ));
            // Check each shift has required fields
            int numberOfShifts = rootNode.get("shifts").size();
            for(int i = 0; i < numberOfShifts; i++) {
                JsonNode notification = rootNode.get("shifts").get(i);
                assertTrue(notification.has("id"));
                assertTrue(notification.has("accountId"));
                assertTrue(notification.has("username"));
                assertTrue(notification.has("rotaType"));
                assertTrue(notification.has("date"));
                assertTrue(notification.has("type"));
                assertTrue(notification.has("ruleNotes"));
                assertTrue(notification.has("accountLevel"));
            }// Check each leave request has required fields
            int numberOfLeaves = rootNode.get("shifts").size();
            for(int i = 0; i < numberOfLeaves; i++) {
                JsonNode notification = rootNode.get("leave").get(i);
                assertTrue(notification.has("id"));
                assertTrue(notification.has("accountId"));
                assertTrue(notification.has("username"));
                assertTrue(notification.has("date"));
                assertTrue(notification.has("type"));
                assertTrue(notification.has("length"));
                assertTrue(notification.has("status"));
                assertTrue(notification.has("note"));
                assertTrue(notification.has("accountLevel"));
            }

            // Check response expecting one shift and one leave request (level 0 account)
            response = GetOperations.getShifts(year, id1);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(1, rootNode.get("shifts").size());
            assertEquals(1, rootNode.get("leave").size());
            // Check values in the shift
            JsonNode shift = rootNode.get("shifts").get(0);
            assertEquals(id1, shift.get("id").asInt());
            assertEquals(id1, shift.get("accountId").asInt());
            assertEquals(username1, shift.get("username").asText());
            assertEquals(4, shift.get("rotaType").asInt());
            assertEquals("1922-09-01", shift.get("date").asText());
            assertEquals(0, shift.get("type").asInt());
            assertTrue(shift.has("ruleNotes"));
            assertEquals(0, shift.get("accountLevel").asInt());
            // Check the values in the leave
            JsonNode leave = rootNode.get("leave").get(0);
            assertTrue(leave.has("id"));
            assertEquals(id1, leave.get("accountId").asInt());
            assertEquals(username1, leave.get("username").asText());
            assertEquals("1922-09-11", leave.get("date").asText());
            assertEquals(0, leave.get("type").asInt());
            assertEquals(0, leave.get("length").asInt());
            assertEquals(0, leave.get("status").asInt());
            assertTrue(leave.has("note"));
            assertEquals(0, leave.get("accountLevel").asInt());

            // Check response expecting no shifts and no leave (no account)
            response = GetOperations.getShifts(year, id3);
            rootNode = mapper.readTree(String.valueOf(response.getBody()));
            assertEquals(0, rootNode.get("shifts").size());
            assertEquals(0, rootNode.get("leave").size());
            assertTrue(response.getBody().toString().contains("{\"shifts\":[],\"leave\":[]}"));
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
    void testGetLogin() throws JsonProcessingException {
        String username = RandomStringUtils.randomAlphanumeric(20);
        String password = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
        // Create account
        PostOperations.postAccount(username);
        // Try wrong username
        assertThrows(ResponseStatusException.class, ()-> GetOperations.getLogin(username+"a", password),
                "Should not allow login for incorrect username");
        // Try wrong password
        assertThrows(ResponseStatusException.class, ()-> GetOperations.getLogin(username, password+"a"),
                "Should not allow login for incorrect username");
        // Try both wrong
        assertThrows(ResponseStatusException.class, ()-> GetOperations.getLogin(username+"a", password+"a"),
                "Should not allow login for incorrect username");
        // Try correct combination
        ResponseEntity<ObjectNode> response = GetOperations.getLogin(username, password);
        // Validate return data
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));
        assertTrue(rootNode.has("token"));
        assertTrue(rootNode.has("accountId"));
        int accountId = rootNode.get("accountId").asInt();
        assertTrue(rootNode.has("level"));
        // Delete account
        DeleteOperations.deleteAccount(accountId);
    }

    @Test
    void testGetRotaGroups() throws JsonProcessingException {
        ResponseEntity<ObjectNode> response = GetOperations.getRotaGroup();
        JsonNode rootNode = new ObjectMapper().readTree(String.valueOf(response.getBody()));
        JsonNode rotaGroups = rootNode.get("rotaGroups");
        int numberOfGroups = rotaGroups.size();
        assertTrue(numberOfGroups >= 1, "There should always be at least one rota group");
        boolean onlyOneTrueStatus = false;
        // Check each group has required fields
        for(int i = 0; i < numberOfGroups; i++) {
            JsonNode rotaGroup = rotaGroups.get(i);
            assertTrue(rotaGroup.has("id"));
            assertTrue(rotaGroup.has("startDate"));
            assertTrue(rotaGroup.has("endDate"));
            assertTrue(rotaGroup.has("status"));
            if(onlyOneTrueStatus && rotaGroup.get("status").asBoolean()) {
                fail("There shouldn't be multiple true/active rota groups.");
            }
            onlyOneTrueStatus = onlyOneTrueStatus || rotaGroup.get("status").asBoolean();
        }
        assertTrue(onlyOneTrueStatus, "Should have one true/active rota group, not none.");
    }

}
