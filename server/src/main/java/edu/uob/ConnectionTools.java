package edu.uob;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;

public class ConnectionTools {

    // Get the database connection string
    public static String getConnectionString() {
        return getEnvOrSysVariable("JDBC_DATABASE_URL");
    }

    // Get any variable from .env file or system
    public static String getEnvOrSysVariable(String variableName) {
        String variable = "";
        // First try and get the variable from .env file
        try {
            variable = Dotenv.configure().load().get(variableName);
        } catch (Exception ignored) {
            //Likely no .env file
        }
        // If not variable available, get from the system instead
        if(variable == null || variable.equals("")) {
            variable = System.getenv(variableName);
        }
        return variable;
    }

    // Get any variable from .env file or system
    public static String getEnvOrSysVariable(String variableName) {
        String variable = "";
        // First try and get the variable from .env file
        try {
            variable = Dotenv.configure().load().get(variableName);
        } catch (Exception ignored) {
            //Likely no .env file
        }
        // If not variable available, get from the system instead
        if(variable == null || variable.equals("")) {
            variable = System.getenv(variableName);
        }
        return variable;
    }

    public static boolean idExistInTable(int id, String columnName, String tableName, Connection c) {
        // Get true or false value for where an id exists in the table
        String SQL = "SELECT EXISTS (SELECT " + columnName + " FROM " + tableName + " WHERE " + columnName + " = ?);";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            r.next();
            return r.getBoolean(1);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    // Whether an account id exists in the account table
    public static boolean accountIdExists(int id, Connection c) {
        // Get true or false value for where an id exists in the accounts table
        return idExistInTable(id, "id", "accounts", c);
    }

    // Check that a token is valid for a particular level of access or higher
    public static boolean validToken(String token, int level) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Does the token match an account where level is equal or greater than given
            String SQL = "SELECT EXISTS (SELECT t.token FROM tokens t LEFT JOIN accounts a ON t.accountid = a.id " +
                    "WHERE a.level >= ? AND t.token = ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, level);
                s.setString(2, token);
                ResultSet r = s.executeQuery();
                r.next();
                return r.getBoolean(1);
            }
        } catch(SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
    
    public static boolean isAdminAccount(int id, Connection c) {
        // Get true or false value for where an id exists in the table
        String SQL = "SELECT level FROM accounts WHERE id = ?; ";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            int level = -1;
            while(r.next()) {
                level = r.getInt("level");
            }
            return level == 1;
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
    
    // Checks token and throws exception if not valid
    public static void validTokenAuthorised(String token, int level) {
        if(!validToken(token, level)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized");
        }
    }

    // Get highest level valid token for testing
    public static String getValidToken() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Newest token first, so token is as fresh as possible
            String SQL = "SELECT t.token FROM tokens t LEFT JOIN accounts a ON t.accountid = a.id WHERE a.level = 1 " +
                    " ORDER BY t.timestamp DESC;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                ResultSet r = s.executeQuery();
                r.next();
                return r.getString("token");
            }
        } catch(SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }
}
