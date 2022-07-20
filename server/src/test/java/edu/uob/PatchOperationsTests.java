package edu.uob;

import org.junit.jupiter.api.Test;
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
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id 998999999 (definitely unused), with some data
            assertFalse(ConnectionTools.accountIdExists(998999999, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (998999999, 'Test Person', 'sdfdfsgghndfh', '987', 'person@test.com', 15, 15, 48, 0); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Change some data (3 bits)
            PatchOperations.updateVariable("person2@test.com", "string", "email", "accounts", 998999999, c);
            PatchOperations.updateVariable("true", "boolean", "fixedWorking", "accounts", 998999999, c);
            PatchOperations.updateVariable("25", "int", "annualLeave", "accounts", 998999999, c);
            // Confirm update has worked
            SQL = "SELECT * FROM accounts WHERE id = 998999999;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals("person2@test.com", r.getString("email"));
                assertTrue(r.getBoolean("fixedWorking"));
                assertEquals(25, r.getInt("annualLeave"));
            }
            // Delete account
            DeleteOperations.deleteAccount(998999999);
            assertFalse(ConnectionTools.accountIdExists(998999999, c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testPatchAccount() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id 919999999 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(919999999, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (919999999, 'Test T. Test', 'sdfdsh', '0987', 'ttt@test.com', 15, 15, 48, 0);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            //Update details
            PatchOperations.patchAccount(919999999, "30", "29", "20", "1", "real@email.com",
                    "+447777777777", "123456789", "0", "0", "0.8", "false");
            //Check details
            SQL = "SELECT email, phone, doctorId, annualLeave, studyLeave, workingHours, accountStatus, doctorStatus, level, timeWorked, fixedWorking FROM accounts WHERE id = 919999999;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals("real@email.com", r.getString("email"));
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
            }
            //Update only some details
            PatchOperations.patchAccount(919999999, null, "39", null, "0", null,
                    "mob: 07777 777777", null, null, null, "0.6", null);
            //Check details
            SQL = "SELECT email, phone, doctorId, annualLeave, studyLeave, workingHours, accountStatus, doctorStatus, level, timeWorked, fixedWorking FROM accounts WHERE id = 919999999;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals("real@email.com", r.getString("email"));
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
            }
            //Update non-existent account
            assertFalse(ConnectionTools.accountIdExists(929999999, c));
            assertThrows(ResponseStatusException.class, ()-> PatchOperations.patchAccount(929999999, "30", "29", "20", "1", "real@email.com",
                    "+447777777777", "123456789", "0", "0", "0.8", "false"));
            // Check incorrect data format
            assertThrows(NumberFormatException.class, ()-> PatchOperations.patchAccount(919999999, "thirty", "29", "20", "1", "real@email.com",
                    "+447777777777", "123456789", "0", "0", "0.8", "false"));
            //Delete account
            DeleteOperations.deleteAccount(919999999);
            assertFalse(ConnectionTools.accountIdExists(919999999, c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testPatchNotification() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            int id1 = 999999073;
            int id2 = 999999074;
            String SQL1 = "DELETE FROM leaveRequests WHERE accountId = 999999073; " +
                    "DELETE FROM notifications WHERE id = 999999703; " +
                    "DELETE FROM accounts WHERE id = 999999073; ";
            try (PreparedStatement s = c.prepareStatement(SQL1)) {
                s.executeUpdate();
            }
            // Create new account with id 999999073 (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            assertFalse(ConnectionTools.idExistInTable(
                    999999730, "id", "leaveRequests", c));
            assertFalse(ConnectionTools.idExistInTable(
                    999999703, "id", "notifications", c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (999999073, 'test073User', 'pwd999999073', '9073', 't_user073@test.com', 15, 15, 48, 0);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Add test data for leave requests and notifications
            SQL = "INSERT INTO leaveRequests (id, accountId, date, type, note, status) " +
                    "VALUES (999999730, 999999073, '1922-07-19', 0, 'note', 0); " +
//                    "(999999740, 999999074, '1923-07-19', 1, 'second note', 1); " +
                    "INSERT INTO notifications (id, type, detailId) " +
                    "VALUES (999999703, 0, 999999730); ";
//                    ", (999999704, 0, 999999740);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
            // Update notification details
            PatchOperations.patchNotification(999999703, id1, "1");
//            // Check details
            SQL = "SELECT N.id, N.type, N.detailId, L.id AS leaveRequestId, L.accountId, L.status " +
                    "FROM notifications N " +
                    "LEFT JOIN leaveRequests L on N.detailId = L.id " +
                    "WHERE L.accountId = ?";
//        "SELECT id, accountId, detailId FROM notifications WHERE id = 999999073;"; //todo delete
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
                ResultSet r = s.executeQuery();
                r.next();
                assertEquals(999999703, r.getInt("id"));
                assertEquals(0, r.getInt("type"));
                assertEquals(999999730, r.getInt("detailId"));
                assertEquals(999999730, r.getInt("leaveRequestId"));
                assertEquals(999999073, r.getInt("accountId"));
                assertEquals(1, r.getInt("status"));
            }
            //Update non-existent account
            assertFalse(ConnectionTools.accountIdExists(1000001001, c));
            assertThrows(ResponseStatusException.class,
                    ()-> PatchOperations.patchNotification(
                            999999703, 1000001001, "2"));
            //Delete account
            DeleteOperations.deleteAccount(id1);
            assertFalse(ConnectionTools.accountIdExists(id1, c));
        } catch (SQLException e) {
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }
}
