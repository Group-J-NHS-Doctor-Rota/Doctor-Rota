package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteOperations {

    public static ResponseEntity<String> deleteAccount(int accountId) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            if(!ConnectionTools.accountIdExists(accountId, c)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id "+accountId+" does not exist");
            }
            // Only if account id exists, then try to delete data
            String SQL = "DELETE FROM notifications WHERE detailId IN (SELECT detailId FROM notifications n " +
                        "LEFT JOIN leaveRequests l ON n.detailId = l.id WHERE accountId = ?) AND type = 0; " +
                    "DELETE FROM partTimeDetails WHERE accountId = ?; " +
                    "DELETE FROM fixedRotaShifts WHERE accountId = ?; " +
                    "DELETE FROM accountRotaTypes WHERE accountId = ?; " +
                    "DELETE FROM accountLeaveRequestRelationships WHERE accountId = ?; " +
                    "DELETE FROM leaveRequests WHERE accountId = ?; " +
                    "DELETE FROM shifts  WHERE accountId = ?; " +
                    "DELETE FROM accounts WHERE id = ?;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                s.setInt(2, accountId);
                s.setInt(3, accountId);
                s.setInt(4, accountId);
                s.setInt(5, accountId);
                s.setInt(6, accountId);
                s.setInt(7, accountId);
                s.setInt(8, accountId);
                s.executeUpdate();
                return ResponseEntity.status(HttpStatus.OK).body("");
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            System.out.println(e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
