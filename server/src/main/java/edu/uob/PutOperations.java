package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PutOperations {

    public static ResponseEntity<String> putWorkingDays(int accountId, boolean monday, boolean tuesday, boolean wednesday,
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
                return ResponseEntity.status(HttpStatus.OK).body("");
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
