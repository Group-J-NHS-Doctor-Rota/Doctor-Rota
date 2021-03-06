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
    void testConnection() {
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

    // Check that you can use a connection to create a table
    void testCreateSQLQuery(Connection c) {
        String SQL = "CREATE TABLE IF NOT EXISTS connectionTestTable (column1 int, column2 varchar);";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            fail("Create SQL query should be valid");
        }
    }

    // Check that you can use a connection to insert data
    void testInsertSQLQuery(Connection c) {
        String SQL = "INSERT INTO connectionTestTable (column1, column2) VALUES (1, 'Aaa');";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            fail("Insert SQL query should be valid");
        }
    }

    // Check that ypu can use a connection to select data
    void testSelectSQLQuery(Connection c) {
        String SQL = "SELECT * FROM connectionTestTable WHERE column1 = 1;";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            ResultSet r = s.executeQuery();
            if(r.next()) {
                int column1 = r.getInt("column1");
                String column2 = r.getString("column2");
                assertEquals(1, column1);
                assertEquals("Aaa", column2);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            fail("Insert SQL query should be valid");
        }
    }

    // Check that you can use a connection to delete a table
    void testDropSQLQuery(Connection c) {
        String SQL = "DROP TABLE connectionTestTable;";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.executeUpdate();
        } catch (SQLException e) {
            fail("Drop SQL query should be valid");
        }
    }

    // Running a series of SQL queries
    // These are designed to create and then destroy without affecting the rest of the tables
    // There should be no evidence of these in the database afterwards
    void testSQLQueries(Connection c) {
        testCreateSQLQuery(c);
        testInsertSQLQuery(c);
        testSelectSQLQuery(c);
        testDropSQLQuery(c);
    }

    @Test
    void testUseConnection() {
        // Test that the connection not only works, but can be uses to execute SQL queries
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            testSQLQueries(c);
        } catch (SQLException e) {
            fail("Database connection should have worked");
        }
    }
}
