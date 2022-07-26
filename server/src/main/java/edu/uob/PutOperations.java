package edu.uob;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;

public class PutOperations {

    public static ResponseEntity<ObjectNode> putWorkingDays(int accountId, boolean monday, boolean tuesday, boolean wednesday,
                                                            boolean thursday, boolean friday, boolean saturday, boolean sunday) {

        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString);) {
            if(!ConnectionTools.accountIdExists(accountId, c)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id "+accountId+" does not exist");
            }
            // Only if account id exists, then try to insert data
            // Delete old data, if any, then insert new data
            String SQL = "DELETE FROM partTimeDetails WHERE accountId = ?;" +
                    "INSERT INTO partTimeDetails (accountId, monday, tuesday, wednesday, thursday, friday, saturday, sunday) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                s.setInt(2, accountId);
                s.setBoolean(3, monday);
                s.setBoolean(4, tuesday);
                s.setBoolean(5, wednesday);
                s.setBoolean(6, thursday);
                s.setBoolean(7, friday);
                s.setBoolean(8, saturday);
                s.setBoolean(9, sunday);
                s.executeUpdate();
                return IndexController.okResponse("Working days put successfully for accountId: " + accountId);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> putFixedShift(int accountId, Date date, int shiftType) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Only if account id exists, then try to insert data
            if(!ConnectionTools.accountIdExists(accountId, c)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Account with id "+accountId+" does not exist");
            }
            // Delete old data, if any, then insert new data
            String SQL = "DELETE FROM fixedRotaShifts WHERE accountId = ? AND date = ?; " +
                    "INSERT INTO fixedRotaShifts (accountId, date, shiftType) VALUES (?, ?, ?); ";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                s.setDate(2, date);
                s.setInt(3, accountId);
                s.setDate(4, date);
//              shiftType: 0: 'Normal Working Day', 1: 'Long Day', 2: 'Night'
                s.setInt(5, shiftType);
                s.executeUpdate();
                return IndexController.okResponse("Fixed shift put successfully for accountId: " + accountId);
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

}
