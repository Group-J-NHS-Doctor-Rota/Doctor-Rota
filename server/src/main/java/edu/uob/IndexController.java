package edu.uob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RestController
public class IndexController {

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<String> index() {
        return ResponseEntity.status(HttpStatus.OK).body("Spring boot server running correctly\n");
    }

    @GetMapping(value = "/test", produces = "application/json")
    public ResponseEntity<String> getTest() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            return ResponseEntity.status(HttpStatus.OK).body("Test ok (CODE 200)\n");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.toString().concat("\nThe request was not completed due to server issues\n"));
        }
    }

    // TODO maybe remove or improve this depending on usage
    @GetMapping(value = "/configvar", produces = "application/json")
    public ResponseEntity<String> getConfigVar() {
        String url = ConnectionTools.getConnectionString();
        String lastFourChars;
        if (url.length() > 4) {
            lastFourChars = url.substring(url.length() - 4);
        } else {
            lastFourChars = "";
        }
        return ResponseEntity.status(HttpStatus.OK).body(lastFourChars.concat("\n"));
    }

    //TODO remove this as just example code
    //https://jenkov.com/tutorials/java-json/jackson-jsonnode.html
    @GetMapping(value = "/testjson", produces = "application/json")
    public ResponseEntity<ObjectNode> testJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("field1", "value1");
        objectNode.put("field2", 123);
        objectNode.put("field3", 999.999);
        ObjectNode objectNode2 = objectMapper.createObjectNode();
        objectNode2.put("item 1", "value1");
        objectNode2.put("item 2", 123);
        objectNode2.put("item 3", 999.999);
        objectNode.putArray("array").add(objectNode2);
        return ResponseEntity.status(HttpStatus.OK).body(objectNode);
    }

    @PutMapping(value = "/account/{id}/workingdays", produces = "application/json")
    public ResponseEntity<String> putWorkingDays(@PathVariable int id, @RequestParam boolean monday, @RequestParam boolean tuesday,
                                                 @RequestParam boolean wednesday, @RequestParam boolean thursday, @RequestParam boolean friday,
                                                 @RequestParam boolean saturday, @RequestParam boolean sunday) {
        //todo check token is valid
        return PutOperations.putWorkingDays(id, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @GetMapping(value = "/shift/{year}", produces = "application/json")
    public ResponseEntity<ObjectNode> getShifts(@RequestParam int accountId, @RequestParam int year) {
        return GetOperations.getShifts(accountId, year);
    }

}
