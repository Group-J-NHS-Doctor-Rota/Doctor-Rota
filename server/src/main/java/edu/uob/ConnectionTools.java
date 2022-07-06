package edu.uob;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;

public class ConnectionTools {

    // Get the database connection string
    public static String getConnectionString() {
        String JDBC_DATABASE_URL = "";
        // First try and get the variable from .env file
        try {
            JDBC_DATABASE_URL = Dotenv.configure().load().get("JDBC_DATABASE_URL");
        } catch (Exception ignored) {
            //Likely no .env file
        }
        // If not variable available, get from the system instead
        if(JDBC_DATABASE_URL == null || JDBC_DATABASE_URL.equals("")) {
            JDBC_DATABASE_URL = System.getenv("JDBC_DATABASE_URL");
        }
        return JDBC_DATABASE_URL;
    }

    // Whether an account id exists in the account table
    public static boolean accountIdExists(int id, Connection c) {
        // Get true or false value for where an id exists in the accounts table
        String SQL = "SELECT EXISTS (SELECT id FROM accounts WHERE id = ?);";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            r.next();
            return r.getBoolean(1);
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
