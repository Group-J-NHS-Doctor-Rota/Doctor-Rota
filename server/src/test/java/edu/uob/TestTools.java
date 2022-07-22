package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;

public class TestTools {

    public static int getTestAccountId() {
        //https://stackoverflow.com/questions/363681/how-do-i-generate-random-integers-within-a-specific-range-in-java
        // Any 9-digit number beginning with 9 is reserved for testing
        return ThreadLocalRandom.current().nextInt(900000000, 1000000000);
    }

    // Only use this function if manually tidying up database
    // NEVER use this function in any tests!!!
    public static void deleteAnyTestData() throws SQLException {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Any 9-digit accountId beginning with 9 is reserved for testing and should be deleted
            String SQL = "DELETE FROM notifications WHERE detailId IN (SELECT detailId FROM notifications n " +
                    "LEFT JOIN leaveRequests l ON n.detailId = l.id WHERE (accountId >= 900000000 AND accountId < 1000000000) ) AND type = 0; " +
                    "DELETE FROM tokens WHERE accountId >= 900000000 AND accountId < 1000000000; " +
                    "DELETE FROM partTimeDetails WHERE accountId >= 900000000 AND accountId < 1000000000; " +
                    "DELETE FROM fixedRotaShifts WHERE accountId >= 900000000 AND accountId < 1000000000; " +
                    "DELETE FROM accountRotaTypes WHERE accountId >= 900000000 AND accountId < 1000000000; " +
                    "DELETE FROM accountLeaveRequestRelationships WHERE accountId >= 900000000 AND accountId < 1000000000; " +
                    "DELETE FROM leaveRequests WHERE accountId >= 900000000 AND accountId < 1000000000; " +
                    "DELETE FROM shifts  WHERE accountId >= 900000000 AND accountId < 1000000000; " +
                    "DELETE FROM accounts WHERE id >= 900000000 AND id < 1000000000;";
            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.executeUpdate();
            }
        }
    }

}
