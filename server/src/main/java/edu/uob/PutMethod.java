package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;

public class PutMethod {

    public static ResponseEntity<String> putFixedShift(int id, Date date, int shift) {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString);) {
            try(PreparedStatement putStatement = c.prepareStatement(
                    "INSERT INTO table_name (date, shift) VALUES (?, ?);")) {
//                putStatement.setInt(1, id); // todo where should I use id?
                putStatement.setDate(1, date);
                putStatement.setInt(2, shift);
                putStatement.executeUpdate();
                putStatement.close();
                return ResponseEntity.status(HttpStatus.OK).body("");
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
