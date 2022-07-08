package edu.uob;

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
import java.util.Map;

public class GetOperationsTest {

    @Test
    void testGetNotifications() {
        ResponseEntity<ObjectNode> response = GetOperations.getNotifications(1);
        System.out.println(response.getBody());
        response = GetOperations.getNotifications(2);
        System.out.println(response.getBody());
        response = GetOperations.getNotifications(3);
        System.out.println(response.getBody());
    }
}
