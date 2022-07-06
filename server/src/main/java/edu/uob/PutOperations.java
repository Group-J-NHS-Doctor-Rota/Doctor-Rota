package edu.uob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class PutOperations {
    public static ResponseEntity<String> putWorkingDays(int id, boolean monday, boolean tuesday, boolean wednesday,
                                                     boolean thursday, boolean friday, boolean saturday, boolean sunday) {
        //todo check id exists
        if(ConnectionTools.accountIdExists(id)) {
            return ResponseEntity.status(HttpStatus.OK).body(id+""+monday+""+tuesday+"test\n");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account id "+id+" does not exist\n");

    }
}
