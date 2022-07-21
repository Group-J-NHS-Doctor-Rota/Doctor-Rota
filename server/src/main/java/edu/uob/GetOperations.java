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
        try(Connection c = DriverManager.getConnection(connectionString)) {
            String SQL = "SELECT N.id, L.id AS leaveRequestId, L.accountId, L.date, L.type, L.note, L.status " +
                    "FROM notifications N " +
                    "LEFT JOIN leaveRequests L on N.detailId = L.id " +
                    "WHERE N.type = 0 " +
                    // If account is admin, get all notifications, else get other their notifications
                    "AND ( ((SELECT level FROM accounts WHERE id = ?) = 1) OR L.accountid = ? ) " +
                    "ORDER BY N.id;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                ArrayNode arrayNode = objectNode.putArray("leaveRequests");
                s.setInt(1, accountId);
                s.setInt(2, accountId);
                ResultSet r = s.executeQuery();
                // Store each notification information in a list
                while(r.next()) {
                    ObjectNode objectNodeRow = new ObjectMapper().createObjectNode();
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
        try(Connection c = DriverManager.getConnection(connectionString)) {
            if(!ConnectionTools.accountIdExists(accountId, c)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id "+accountId+" does not exist");
            }
            // Only if account id exists, then get single account and store as key value
            String SQL = "SELECT * FROM accounts WHERE id = ?;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
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
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Get al accounts and store in list
            String SQL = "SELECT * FROM accounts ORDER BY id;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                ArrayNode arrayNode = objectNode.putArray("accounts");
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    ObjectNode objectNodeRow = new ObjectMapper().createObjectNode();
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

    // Needed for get account and get all accounts
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

    public static ResponseEntity<ObjectNode> getShifts(int year, int accountId) {
        String connectionString = ConnectionTools.getConnectionString();
        try (Connection c = DriverManager.getConnection(connectionString)) {
            String SQL = "SELECT S.id, S.accountId, A.username, S.rotaTypeId, " +
                    "S.date, S.type, S.ruleNotes, A.level " +
                    "FROM shifts S " +
                    "LEFT JOIN accounts A ON S.accountId = A.id " +
                    "WHERE S.date::text LIKE '" + year + "%' " +
                    // If account is admin, get all shifts, else get other their shifts
                    "AND ( ((SELECT level FROM accounts WHERE id = ?) = 1) OR S.accountId = ? );";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                ArrayNode arrayNode = objectNode.putArray("shifts");
                s.setInt(1, accountId); // In SQL sentence, WHERE id = ?
                s.setInt(2, accountId); // In SQL sentence, OR S.accountId = ?
                ResultSet r = s.executeQuery();
                while (r.next()) {
                    ObjectNode objectNodeRow = new ObjectMapper().createObjectNode();
                    // r.getXXX(table's name)
                    objectNodeRow.put("id", r.getInt("id"));
                    objectNodeRow.put("accountId", r.getInt("accountId"));
                    objectNodeRow.put("username", r.getString("username"));
                    objectNodeRow.put("rotaType", r.getInt("rotaTypeId"));
                    objectNodeRow.put("date", String.valueOf(r.getDate("date")));
                    // type: 0: Normal working day, 1: Long Day, 2: Night
                    objectNodeRow.put("type", r.getInt("type"));
                    objectNodeRow.put("ruleNotes", r.getString("ruleNotes"));
                    objectNodeRow.put("accountLevel", r.getInt("level"));
                    arrayNode.add(objectNodeRow);
                }
                return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> getLeaves(int accountId) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            String SQL = "SELECT id, annualLeave, studyLeave FROM accounts WHERE id = ?; ";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                s.setInt(1, accountId); // In SQL sentence, WHERE id = ?
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    objectNode.put("id", r.getInt("id"));
                    objectNode.put("studyLeave", r.getInt("studyLeave"));
                    objectNode.put("annualLeave", r.getInt("annualLeave"));
                }
                return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
}
