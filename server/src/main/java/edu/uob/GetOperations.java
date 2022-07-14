package edu.uob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;

public class GetOperations {

    public static ResponseEntity<ObjectNode> getNotifications(int accountId) {

        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString);) {
            //
            String SQL = "SELECT N.id, L.id AS leaveRequestId, L.accountId, L.date, L.type, L.note, L.status " +
                    "FROM notifications N " +
                    "LEFT JOIN leaveRequests L on N.detailId = L.id " +
                    "WHERE N.type = 0 " +
                    // If account is admin, get all notifications, else get other their notifications
                    "AND ( ((SELECT level FROM accounts WHERE id = ?) = 1) OR L.accountid = ? ) " +
                    "ORDER BY N.id;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                ArrayNode arrayNode = objectNode.putArray("leaveRequests");
                s.setInt(1, accountId);
                s.setInt(2, accountId);
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    ObjectNode objectNodeRow = objectMapper.createObjectNode();
                    objectNodeRow.put("id", r.getInt("id"));
                    objectNodeRow.put("leaveRequestId", r.getInt("leaveRequestId"));
                    objectNodeRow.put("accountId", r.getInt("accountId"));
                    objectNodeRow.put("date", r.getString("date"));
                    objectNodeRow.put("type", r.getInt("type"));
                    objectNodeRow.put("note", r.getString("note"));
                    objectNodeRow.put("status", r.getInt("status"));
                    arrayNode.add(objectNodeRow);
                }
                return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> getAccount(int accountId) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString);) {
            //
            String SQL = "SELECT * FROM accounts WHERE id = ?;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                ResultSet r = s.executeQuery();
                if(r.next()) {
                    addAccountDetailsToObjectNode(r, objectNode);
                }
                return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> getAllAccounts() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString);) {
            //
            String SQL = "SELECT * FROM accounts ORDER BY id;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                ArrayNode arrayNode = objectNode.putArray("accounts");
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    ObjectNode objectNodeRow = objectMapper.createObjectNode();
                    addAccountDetailsToObjectNode(r, objectNodeRow);
                    arrayNode.add(objectNodeRow);
                }
                return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    private static void addAccountDetailsToObjectNode(ResultSet r, ObjectNode objectNode) throws SQLException {
        objectNode.put("id", r.getInt("id"));
        objectNode.put("username", r.getString("username"));
        objectNode.put("email", r.getString("email"));
        objectNode.put("phone", r.getString("phone"));
        objectNode.put("doctorId", r.getString("doctorId"));
        objectNode.put("annualLeave", r.getInt("annualLeave"));
        objectNode.put("studyLeave", r.getInt("studyLeave"));
        objectNode.put("workingHours", r.getInt("workingHours"));
        objectNode.put("accountStatus", r.getInt("accountStatus"));
        objectNode.put("doctorStatus", r.getInt("doctorStatus"));
        objectNode.put("level", r.getInt("level"));
        objectNode.put("timeWorked", r.getFloat("timeWorked"));
        objectNode.put("fixedWorking", r.getBoolean("fixedWorking"));

    }
}
