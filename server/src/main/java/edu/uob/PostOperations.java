package edu.uob;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;

public class PostOperations {
    public static ResponseEntity<ObjectNode> postRequestLeave(int accountId, String date, int type, int length, String note) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            if(!ConnectionTools.accountIdExists(accountId, c)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id "+accountId+" does not exist");
            }
            // Only if account id exists, then try to insert data
            // Delete old data, if any, then insert new data
            String SQL = "DELETE FROM notifications WHERE detailId IN (SELECT detailId FROM notifications n " +
                    "LEFT JOIN leaveRequests l ON n.detailId = l.id WHERE accountId = ? AND date = ?) AND type = 0; " +
                    "DELETE FROM leaveRequests WHERE accountId = ? AND date = ?; " +
                    "INSERT INTO leaveRequests (accountId, date, type, length, note, status) " +
                    "VALUES (?, ?, ?, ?, ?, 0); " +
                    "INSERT INTO notifications (type, detailId) VALUES " +
                    "(0, (SELECT id FROM leaveRequests WHERE accountId = ? AND date = ?) ); ";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                s.setDate(2, Date.valueOf(date));
                s.setInt(3, accountId);
                s.setDate(4, Date.valueOf(date));
                s.setInt(5, accountId);
                s.setDate(6, Date.valueOf(date));
                s.setInt(7, type);
                s.setInt(8, length);
                s.setString(9, note);
                s.setInt(10, accountId);
                s.setDate(11, Date.valueOf(date));
                s.executeUpdate();
                return IndexController.okResponse("Leave request successful for accountId: " + accountId);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> postAccount(String username, String email) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Check if username already exists
            String SQL = "SELECT EXISTS (SELECT username FROM accounts WHERE username = ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, username);
                ResultSet r = s.executeQuery();
                r.next();
                if(r.getBoolean(1)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Account with username "+username+" already exists");
                }
            }
            // Generate unique salt
            Encryption encryption = new Encryption();
            // Get pepper and default password
            String default_password = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
            // Create hashed password
            String hashed_password = encryption.hashPassword(default_password);
            // Store information in database
            SQL = "INSERT INTO accounts (username, password, email) " +
                    "VALUES (?, ?, ?); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, username);
                s.setString(2, hashed_password);
                s.setString(3, email);
                s.executeUpdate();
            }
            // Create and store token
            SQL = "INSERT INTO tokens (accountid, token) VALUES ((SELECT id FROM accounts WHERE username = ?), ?); ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, username);
                s.setString(2, Encryption.getRandomToken());
                s.executeUpdate();
            }
            return IndexController.okResponse("Account creation successful for username: " + username);
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> postAccount(String username) {
        return postAccount(username, null);
    }
}
