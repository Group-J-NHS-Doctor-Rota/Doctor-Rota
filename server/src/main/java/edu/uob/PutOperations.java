package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PutOperations {
    public static ResponseEntity<String> workingdays(String id, boolean monday, boolean tuesday, boolean wednesday,
                                                     boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        return ResponseEntity.status(HttpStatus.OK).body(id+monday+tuesday+"test\n");
    }
}
