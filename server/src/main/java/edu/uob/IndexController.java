package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
public class IndexController {

    @GetMapping("/")
    public ResponseEntity<String> index() {
        return ResponseEntity.status(HttpStatus.OK).body("Spring boot server running correctly (CODE 200)\n");
    }

    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            return ResponseEntity.status(HttpStatus.OK).body("Test ok (CODE 200)\n");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage().concat("\nThe request was not completed due to database connection issues (CODE 500)\n"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage().concat("\nThe request was not completed due to server issues (CODE 500)\n"));
        }
    }

    @GetMapping("/configvar")
    public ResponseEntity<String> getConfigVar() {
        String url = System.getenv("JDBC_DATABASE_URL");
        String lastFourChars;
        if (url.length() > 4) {
            lastFourChars = url.substring(url.length() - 4);
        } else {
            lastFourChars = url;
        }
        return ResponseEntity.status(HttpStatus.OK).body(lastFourChars.concat("\n"));
    }
}
