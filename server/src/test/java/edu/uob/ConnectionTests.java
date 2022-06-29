package edu.uob;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConnectionTests {

    @Test
    void testGetConnectionString() {
        String connectionString = ConnectionTools.getConnectionString();
        // Check connection sting has been found, so is not empty or null
        assertNotEquals("", connectionString);
        assertNotEquals(null, connectionString);
    }

    @Test
    void testConnection() throws SQLException {
        // Cannot connect with empty or incorrect connection strings
        assertThrows(SQLException.class, ()->DriverManager.getConnection(null));
        assertThrows(SQLException.class, ()->DriverManager.getConnection(""));
        assertThrows(SQLException.class, ()->DriverManager.getConnection("Some/Gibberish"));
        // Correct connection string should allow for connection with no issue
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            assertTrue(true, "Database connection should have worked");
        } catch (SQLException e) {
            fail("Database connection should have worked");
        }
    }
}
