package edu.uob;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

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

}
