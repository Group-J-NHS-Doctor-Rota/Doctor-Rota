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
            //TODO delete data in notifications too?
            String SQL = "DELETE FROM leaveRequests WHERE accountId = ? AND date = ?;" +
                    "INSERT INTO leaveRequests (accountId, date, type, length, note, status) " +
                    "VALUES (?, ?, ?, ?, ?, 0);";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                s.setDate(2, Date.valueOf(date));
                s.setInt(3, accountId);
                s.setDate(4, Date.valueOf(date));
                s.setInt(5, type);
                s.setInt(6, length);
                s.setString(7, note);
                s.executeUpdate();
                return ResponseEntity.status(HttpStatus.OK).body("");
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
}
