package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PostOperations {
    public static ResponseEntity<String> postRequestLeave(int accountId, String date, int type, String note) {
        return ResponseEntity.status(HttpStatus.OK).body("");
    }
}
