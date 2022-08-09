package edu.uob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.util.Calendar;

public class GetOperations {

    public static ResponseEntity<ObjectNode> getNotifications(int accountId) {

        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            String SQL = "SELECT N.id, L.id AS leaveRequestId, L.accountId, L.date, L.type, L.note, L.status, L.length " +
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
                    objectNodeRow.put("length", r.getInt("length"));
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
            // Get all accounts and store in list
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

    // Gets shifts and leave
    // TODO: this might need refactoring
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
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            ArrayNode arrayNode1 = objectNode.putArray("shifts");
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId); // In SQL sentence, WHERE id = ?
                s.setInt(2, accountId); // In SQL sentence, OR S.accountId = ?
                ResultSet r = s.executeQuery();
                while (r.next()) {
                    ObjectNode objectNodeRow = new ObjectMapper().createObjectNode();
                    objectNodeRow.put("id", r.getInt("id"));
                    objectNodeRow.put("accountId", r.getInt("accountId"));
                    objectNodeRow.put("username", r.getString("username"));
                    objectNodeRow.put("rotaType", r.getInt("rotaTypeId"));
                    objectNodeRow.put("date", String.valueOf(r.getDate("date")));
                    // type: 0: Normal working day, 1: Long Day, 2: Night
                    objectNodeRow.put("type", r.getInt("type"));
                    objectNodeRow.put("ruleNotes", r.getString("ruleNotes"));
                    objectNodeRow.put("accountLevel", r.getInt("level"));
                    arrayNode1.add(objectNodeRow);
                }
            }
            // Add in leave request information too
            SQL = "SELECT R.id, R.accountid, A.username, R.date, R.type, R.length, " +
                    "R.status, R.note, A.level FROM leaverequests R " +
                    // Only get request or approved leave request, not rejected
                    "LEFT JOIN accounts A ON R.accountid = A.id WHERE R.status IN (0, 1) " +
                    "AND R.date::text LIKE '" + year + "%' " +
                    // If account is admin, get all leave request, else get other their leave
                    "AND ( ((SELECT level FROM accounts WHERE id = ?) = 1) OR R.accountId = ? );";
            ArrayNode arrayNode2 = objectNode.putArray("leave");
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId); // In SQL sentence, WHERE id = ?
                s.setInt(2, accountId); // In SQL sentence, OR S.accountId = ?
                ResultSet r = s.executeQuery();
                while (r.next()) {
                    ObjectNode objectNodeRow = new ObjectMapper().createObjectNode();
                    objectNodeRow.put("id", r.getInt("id"));
                    objectNodeRow.put("accountId", r.getInt("accountId"));
                    objectNodeRow.put("username", r.getString("username"));
                    objectNodeRow.put("date", String.valueOf(r.getDate("date")));
                    // type: 0: Normal working day, 1: Long Day, 2: Night
                    objectNodeRow.put("type", r.getInt("type"));
                    objectNodeRow.put("length", r.getInt("length"));
                    objectNodeRow.put("status", r.getInt("status"));
                    objectNodeRow.put("note", r.getString("note"));
                    objectNodeRow.put("accountLevel", r.getInt("level"));
                    arrayNode2.add(objectNodeRow);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> getLeaves(int accountId) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Query data of current year
            //todo use join instead of executed 2 queries
            String SQL = "SELECT type, length, status FROM leaveRequests " +
                    "WHERE accountId = ? AND status = 1 AND (date::text LIKE '" +
                    Calendar.getInstance().get(Calendar.YEAR) + "%'); ";
            // status = 1 means 'approved'
            double usedAnnualLeaves = 0.0;
            double usedStudyLeaves = 0.0;
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId); // In SQL sentence, WHERE accountId = ?
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    int leaveType = r.getInt("type");
                    int length = r.getInt("length");
                    if (leaveType == 0) {
                        usedAnnualLeaves += usedDaysCalculator(length);
                    } else if (leaveType == 1) {
                        usedStudyLeaves += usedDaysCalculator(length);
                    } else {
                        // Maybe we will have more leaveRequest types
                        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
                    }
                }
            }
            SQL = "SELECT id, annualLeave, studyLeave FROM accounts WHERE id = ?; ";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                s.setInt(1, accountId); // In SQL sentence, WHERE id = ?
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    double studyLeaves = r.getInt("studyLeave") - usedStudyLeaves;
                    double annualLeaves = r.getInt("annualLeave") - usedAnnualLeaves;
                    objectNode.put("id", r.getInt("id"));
                    objectNode.put("studyLeave", studyLeaves);
                    objectNode.put("annualLeave", annualLeaves);
                }
                return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

    }

    private static double usedDaysCalculator(int length) {
        switch (length) {
            case 0 -> {
                return 1.0;
            }
            case 1, 2 -> {
                return 0.5;
            }
            // Maybe we will have more length types
            default -> throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    public static ResponseEntity<ObjectNode> getLogin(String username, String password) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Check username exists
            String SQL = "SELECT EXISTS (SELECT username FROM accounts WHERE username = ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, username);
                ResultSet r = s.executeQuery();
                r.next();
                if(!r.getBoolean(1)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Account and password combination is incorrect");
                }
            }
            // Get information for that user
            SQL = "SELECT a.id, a.password, a.level, t.token FROM accounts a " +
                    "LEFT JOIN tokens t ON a.id = t.accountId WHERE a.username = ?; ";
            int accountId;
            String hashedPassword;
            int level;
            String token;
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, username);
                ResultSet r = s.executeQuery();
                r.next();
                accountId = r.getInt("id");
                hashedPassword = r.getString("password");
                level = r.getInt("level");
                token = r.getString("token");
            }
            // Check password is correct
            Encryption encryption = new Encryption();
            if(!encryption.passwordMatches(password, hashedPassword)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Account and password combination is incorrect");
            }
            // Return information for user
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            objectNode.put("token", token);
            objectNode.put("accountId", accountId);
            objectNode.put("level", level);
            return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> getRotaGroup() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Get all rotaGroup data
            String SQL = "SELECT * FROM rotaGroups; ";
            ObjectNode objectNode = new ObjectMapper().createObjectNode();
            ArrayNode arrayNode = objectNode.putArray("rotaGroups");
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    ObjectNode objectNodeRow = new ObjectMapper().createObjectNode();
                    objectNodeRow.put("id", r.getInt("id"));
                    objectNodeRow.put("startDate", r.getString("startDate"));
                    objectNodeRow.put("endDate", r.getString("endDate"));
                    objectNodeRow.put("status", r.getBoolean("status"));
                    arrayNode.add(objectNodeRow);
                }
            }
            return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

}
