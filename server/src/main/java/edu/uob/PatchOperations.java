package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatchOperations {

    public static ResponseEntity<String> patchAccount(int accountId, String annualLeave) {
        String connectionString = ConnectionTools.getConnectionString();
        try (Connection c = DriverManager.getConnection(connectionString)) {
            // Update all variables (only provided non-null ones which actually get updated
            updateVariable(annualLeave, "annualLeave", "accounts", accountId, c);

            return ResponseEntity.status(HttpStatus.OK).body("");
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // By design, this will not error if null but just won't run
    public static void updateVariable(String value, String name, String table, int accountId, Connection c) {
        if(value != null) {
            String SQL = "UPDATE " + table + " SET " + name + " = ?, timestamp = now() WHERE id = ?;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, Integer.parseInt(value));
                s.setInt(2, accountId);
                s.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

}
