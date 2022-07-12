package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PatchOperations {

    public static ResponseEntity<String> patchAccount(int accountId, String annualLeave, String studyLeave,
                                                      String workingHours, String level, String email, String phone,
                                                      String doctorId, String accountStatus, String doctorStatus,
                                                      String timeWorked, String fixedWorking) {
        String connectionString = ConnectionTools.getConnectionString();
        try (Connection c = DriverManager.getConnection(connectionString)) {
            // Update all variables (only non-null ones actually get updated)
            updateVariable(annualLeave, "int", "annualLeave", "accounts", accountId, c);
            updateVariable(studyLeave, "int", "studyLeave", "accounts", accountId, c);
            updateVariable(workingHours, "int", "workingHours", "accounts", accountId, c);
            updateVariable(level, "int", "level", "accounts", accountId, c);
            updateVariable(email, "string", "email", "accounts", accountId, c);
            updateVariable(phone, "string", "phone", "accounts", accountId, c);
            updateVariable(doctorId, "string", "doctorId", "accounts", accountId, c);
            updateVariable(accountStatus, "int", "accountStatus", "accounts", accountId, c);
            updateVariable(doctorStatus, "int", "doctorStatus", "accounts", accountId, c);
            updateVariable(timeWorked, "float", "timeWorked", "accounts", accountId, c);
            updateVariable(fixedWorking, "boolean", "fixedWorking", "accounts", accountId, c);
            return ResponseEntity.status(HttpStatus.OK).body("");
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    // By design, this will not error if null but just won't run
    public static void updateVariable(String value, String type, String name, String table, int accountId, Connection c) {
        if(value != null) {
            String SQL = "UPDATE " + table + " SET " + name + " = ?, timestamp = now() WHERE id = ?;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                // Convert to the required type when setting
                switch (type) {
                    case "int" -> s.setInt(1, Integer.parseInt(value));
                    case "float" -> s.setFloat(1, Float.parseFloat(value));
                    case "boolean" -> s.setBoolean(1, Boolean.parseBoolean(value));
                    default -> s.setString(1, value);
                }
                s.setInt(2, accountId);
                s.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
            }
        }
    }

}
