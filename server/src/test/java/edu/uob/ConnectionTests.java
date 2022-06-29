package edu.uob;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTests {

    @Test
    void testGetConnectionString() {
        String connectionString = ConnectionTools.getConnectionString();
        assertNotEquals("", connectionString);
        assertNotEquals(null, connectionString);
    }
}
