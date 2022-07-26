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

}
