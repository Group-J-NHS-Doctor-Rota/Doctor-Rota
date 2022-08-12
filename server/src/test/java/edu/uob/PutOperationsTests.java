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

public class PutOperationsTests {

    @Test
    void testPutWorkingDays() {
        // Get random id and username to test
        int id1 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, email) " +
                    "VALUES (?, ?, 'ndsjkfgndsfpgn', 'mctester@test.com');";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, username1);
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
        // Get random id and username to test
        int id1 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            Date date1 = Date.valueOf("1922-07-18");
            // Create new account with id (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, email) " +
                    "VALUES (?, ?, 'pwd999999070', 'test_user070@test.com');";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, username1);
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));

            // Try to put fixed rota shifts for an account id which doesn't exist (1000000000)
            assertFalse(ConnectionTools.accountIdExists(1000000000, c));
            assertThrows(ResponseStatusException.class,
                    ()-> PutOperations.putFixedShift(1000000000, date1, 1));

            // Try to put fixed shift using invalid shiftType
            assertThrows(ResponseStatusException.class,
                    ()-> PutOperations.putFixedShift(id1, date1, 7));
            // Try to put fixed rota shifts for new account 999999070
            PutOperations.putFixedShift(id1, date1, 1);
            // Check row exists and values are correct
            SQL = "SELECT accountId, date, shiftType FROM fixedRotaShifts WHERE accountId = ? AND date = ?; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setDate(2, date1);
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
            SQL = "SELECT accountId, date, shiftType FROM fixedRotaShifts WHERE accountId = ? AND date = ?; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setDate(2, date2);
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

    @Test
    public void testPutAccountRotaType() throws JsonProcessingException {
        // Get random id and username to test
        int id1 = TestTools.getTestAccountId();
        String username1 = TestTools.getRandomUsername();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, email) " +
                    "VALUES (?, ?, 'pwd999999070', 'test_user070@test.com');";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                s.setString(2, username1);
                s.executeUpdate();
            }
            // Check account creation
            assertTrue(ConnectionTools.accountIdExists(id1, c));
            // Add some rota type dates
            PutOperations.putAccountRotaType(id1, 1, "2001-01-01", "2001-02-01");
            PutOperations.putAccountRotaType(id1, 2, "2001-01-01", "2001-03-01");
            PutOperations.putAccountRotaType(id1, 2, "2001-02-01", "2001-09-01");
            PutOperations.putAccountRotaType(id1, 3, "2001-04-01", "2001-07-01");
            PutOperations.putAccountRotaType(id1, 4, "2001-03-01", "2001-05-01");
            // Check stored results are as expected
            ResponseEntity<ObjectNode> response = GetOperations.getAccount(id1);
            JsonNode rootNode = new ObjectMapper().readTree(String.valueOf(response.getBody()));
            JsonNode accountRotaTypes = rootNode.get("accountRotaTypes");
            assertEquals(4, accountRotaTypes.size(), "Should only be 4 as 1st input is completely overlapped by second");
            JsonNode accountRotaType0 = accountRotaTypes.get(0);
            assertEquals(2, accountRotaType0.get("rotaTypeId").asInt());
            assertEquals("2001-01-01", accountRotaType0.get("startDate").asText(), "Should start as input");
            assertEquals("2001-01-31", accountRotaType0.get("endDate").asText(), "Should end early as 3rd overlaps");
            JsonNode accountRotaType1 = accountRotaTypes.get(1);
            assertEquals(2, accountRotaType1.get("rotaTypeId").asInt());
            assertEquals("2001-02-01", accountRotaType1.get("startDate").asText(), "Should start as input");
            assertEquals("2001-02-28", accountRotaType1.get("endDate").asText(), "Should end early as 4th overlaps");
            JsonNode accountRotaType2 = accountRotaTypes.get(2);
            assertEquals(3, accountRotaType2.get("rotaTypeId").asInt());
            assertEquals("2001-05-02", accountRotaType2.get("startDate").asText(), "Should start late as 5th overlaps");
            assertEquals("2001-07-01", accountRotaType2.get("endDate").asText(), "Should end as input");
            JsonNode accountRotaType3 = accountRotaTypes.get(3);
            assertEquals(4, accountRotaType3.get("rotaTypeId").asInt());
            assertEquals("2001-03-01", accountRotaType3.get("startDate").asText(), "Should be as 5th input");
            assertEquals("2001-05-01", accountRotaType3.get("endDate").asText(), "Should be as 5th input");
            // Delete all test data
            DeleteOperations.deleteAccount(id1);
            // Check delete
            assertFalse(ConnectionTools.accountIdExists(id1, c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }
}
