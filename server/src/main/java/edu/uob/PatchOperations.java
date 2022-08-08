package edu.uob;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.sql.*;

public class PatchOperations {

    public static ResponseEntity<ObjectNode> patchAccount(int accountId, String annualLeave, String studyLeave,
                                                      String workingHours, String level, String email, String phone,
                                                      String doctorId, String accountStatus, String doctorStatus,
                                                      String timeWorked, String fixedWorking) {
        String connectionString = ConnectionTools.getConnectionString();
        try (Connection c = DriverManager.getConnection(connectionString)) {
            if(!ConnectionTools.accountIdExists(accountId, c)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id "+accountId+" does not exist");
            }
            // Only if account id exists, then try to put data
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
            return IndexController.okResponse("Account data updated successfully for accountId: " + accountId);
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    // By design, this will not error if null but just won't run
    public static void updateVariable(String value, String type, String name, String table, int id, Connection c) {
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
                s.setInt(2, id);
                s.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
            }
        }
    }

    public static ResponseEntity<ObjectNode> patchNotification(int notificationId, int accountId, String status) {
        String connectionString = ConnectionTools.getConnectionString();
        try (Connection c = DriverManager.getConnection(connectionString)) {
            // Only if account id exists, then try to patch data
            if(!ConnectionTools.accountIdExists(accountId, c)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account with id "+accountId+" does not exist");
            }
            // If status is not 1 or not 2,
            // we cannot update the status in table 'leaveRequest' and 'accountLeaveRequestRelationships'
            if (!status.equals("1") && !status.equals("2")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Invalid request status value: " + status);
            }
            // Only if account level = 1 (admin account), then try to patch data
            // todo use validToken
            String SQL = "SELECT type, detailId FROM notifications WHERE id = ?; ";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, notificationId);
                ResultSet r = s.executeQuery();
                int detailId = -1;
                while(r.next()){
                    int type = r.getInt("type");
                    detailId = r.getInt("detailId");
                    String tableName;
                    switch (type) {
                        case 0 -> tableName = "leaveRequests"; //todo not hard code
                        case 1 -> tableName = ""; // '1' refers to other requests currently
                        default -> tableName = "";
                    }
                    // If the table name is empty, it will throw a new exception in updateVariable().
                    // So I don't need to check table name here. Same with detailId.
                    // Update status in target table
                    updateVariable(status, "int", "status", tableName, detailId, c);
                    newRelationship(accountId, detailId, Integer.parseInt(status), c);
                }
                return IndexController.okResponse("Notification updated successfully for id: " +
                        notificationId + "\nRelationship updated successfully for id: " + detailId);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
    
    public static ResponseEntity<ObjectNode> patchPassword(String oldPassword, String newPassword, int accountId) {
        // Check that passwords do not match
        if(oldPassword.equals(newPassword)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "New password cannot be the same as the old password!\n");
        }
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Get data for accountId
            String SQL = "SELECT password FROM accounts WHERE id = ?;";
            String oldHashedPassword;
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, accountId);
                ResultSet r = s.executeQuery();
                r.next();
                oldHashedPassword = r.getString("password");
            }
            // Validate password
            Encryption encryption = new Encryption();
            if(!encryption.passwordMatches(oldPassword, oldHashedPassword)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Incorrect password");
            }
            // Generate and store new hashed password
            String newHashedPassword = encryption.hashPassword(newPassword);
            SQL = "UPDATE accounts SET password = ?, timestamp = now() WHERE id = ?; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, newHashedPassword);
                s.setInt(2, accountId);
                s.executeUpdate();
            }
            return IndexController.okResponse("Password updated successfully for accountId: " + accountId);
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> patchPasswordReset(String username, String email) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Check account exists for username and password
            String SQL = "SELECT EXISTS (SELECT id FROM accounts WHERE username = ? AND email = ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, username);
                s.setString(2, email);
                ResultSet r = s.executeQuery();
                r.next();
                if(!r.getBoolean(1)) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "No account with that username and email combination");
                }
            }
            // Generate and store new hashed password
            Encryption encryption = new Encryption();
            String defaultPassword = ConnectionTools.getEnvOrSysVariable("DEFAULT_PASSWORD");
            String hashedPassword = encryption.hashPassword(defaultPassword);
            SQL = "UPDATE accounts SET password = ?, timestamp = now() WHERE username = ?; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, hashedPassword);
                s.setString(2, username);
                s.executeUpdate();
                // send a email to user:
                EmailTools emailTools = new EmailTools();
                String msg = emailTools.passwordResetMsg(hashedPassword);
                emailTools.sendSimpleMessage(email, "reset", msg);
            }
            return IndexController.okResponse("Password reset successfully for username: " + username);
            // Have to catch SQLException exception here
        } catch (SQLException | MessagingException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    public static ResponseEntity<ObjectNode> patchLogout(String token) {
        // Update token
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            String SQL = "UPDATE tokens SET token = ?, timestamp = now() WHERE token = ?; ";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setString(1, Encryption.getRandomToken());
                s.setString(2, token);
                s.executeUpdate();
                return IndexController.okResponse("User logged out");
                // Have to catch SQLException exception here
            }
        } catch(SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
    
    public static void newRelationship(int accountId, int leaveRequestId, int status, Connection c) {
        String SQL = "INSERT INTO accountLeaveRequestRelationships (accountId, leaveRequestId, status) " +
                "VALUES (?, ?, ?); ";
        try(PreparedStatement s = c.prepareStatement(SQL)) {
            // VALUES(?, ?, ?)
            s.setInt(1, accountId);
            s.setInt(2, leaveRequestId);
            s.setInt(3, status);
            s.executeUpdate();
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

    }
    
}
