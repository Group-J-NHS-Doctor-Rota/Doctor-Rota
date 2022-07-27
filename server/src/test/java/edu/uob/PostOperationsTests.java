package edu.uob;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.*;

public class PostOperationsTests {

    @Test
    void testPostLeaveRequest() throws SQLException {
        // Get random id to test
        int id1 = TestTools.getTestAccountId();
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Create new account with id (definitely unused)
            assertFalse(ConnectionTools.accountIdExists(id1, c));
            String SQL = "INSERT INTO accounts (id, username, password, salt, email, annualLeave, studyLeave, workingHours, level) " +
                    "VALUES (?, 'Testy Tim', 'dfjghkjhk', 's6w7ju', 'tim@test.com', 15, 15, 48, 0);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id1);
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
            TestTools.deleteAnyTestData();
            fail("Database connection and SQL queries should have worked\n" + e);
        }
    }

    @Test
    void testPostAccount() {
        // Make new account
        String username = RandomStringUtils.randomAlphabetic(20);
        String email = RandomStringUtils.randomAlphabetic(16);
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
        String username = RandomStringUtils.randomAlphabetic(20);
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
    void notTest() {
        Encryption encryption = new Encryption();
        //String salt = encryption.getRandomSalt();
        // Get default password
        String default_password = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");

        // Create hashed password
        String hashed_password1 = encryption.hashPassword(default_password);
        String hashed_password2 = encryption.hashPassword(default_password);
        String hashed_password3 = encryption.hashPassword(default_password);
        System.out.println(encryption.passwordMatches(default_password, hashed_password1));
        System.out.println(encryption.passwordMatches(default_password, hashed_password2));
        System.out.println(encryption.passwordMatches(default_password, hashed_password3));
        System.out.println(encryption.passwordMatches(default_password, "6c92c731386b62a923126c9606da5a87b8ac658f3dedccea340f73e579488bd9f42738fb6e2fba5e"));
        System.out.println(encryption.passwordMatches(default_password, "2c2448b91c025358e7d1b32611aea7eb3c223727a20f83fd1fa3d858e16bf805c9e2946c40e9ea66"));
        System.out.println(hashed_password1 + "\n" + hashed_password2 + "\n" + hashed_password3);

    }

}
