package edu.uob;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import java.sql.*;

public class ConnectionTools {

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

    public static boolean accountIdExists(int id) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            String SQL = "SELECT EXISTS (SELECT id FROM accounts WHERE id = ?);";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id);
                ResultSet r = s.executeQuery();
                r.next();
                return r.getBoolean(1);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}
