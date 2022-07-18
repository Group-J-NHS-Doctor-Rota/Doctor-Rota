package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class PostOperations {
    public static ResponseEntity<String> postRequestLeave(int accountId, String date, int type, int length, String note) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString);) {
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
                return ResponseEntity.status(HttpStatus.OK).body("");
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
}
