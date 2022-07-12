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
    //TODO this test
    void testUpdateVariable() {

    }

    @Test
    void testPatchAccount() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id 991999999 (definitely unused)
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
}
