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
                    "AND ( ((SELECT level FROM accounts WHERE id = ?) = 1) OR L.accountid = ? );";
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<ObjectNode> getShifts(int accountId, int year) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
//id int
//accountId int
//username varchar -- left join？？
//rotaType int
//date date
//type int
//ruleNotes varchar
//accountLevel int -- left join？？
            String SQL = "SELECT S.id, S.accountId, A.username, S.rotaTypeId, " +
                    "S.date, S.type, S.ruleNotes, A.level " +
                    "FROM shifts S " +
                    "LEFT JOIN accounts A ON S.accountId = A.id " +
                    "WHERE S.date::text LIKE '" + year + "%'; ";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode objectNode = objectMapper.createObjectNode();
                ArrayNode arrayNode = objectNode.putArray("shifts");
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    ObjectNode objectNodeRow = objectMapper.createObjectNode();
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
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
}
