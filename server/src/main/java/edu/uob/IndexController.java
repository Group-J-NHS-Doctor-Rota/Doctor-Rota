package edu.uob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Date;

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
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.toString().concat("\nThe request was not completed due to server issues (CODE 500)\n"));
        }
    }

    // TODO maybe remove or improve this depending on usage
    @GetMapping("/configvar")
    public ResponseEntity<String> getConfigVar() {
        String url = System.getenv("JDBC_DATABASE_URL");
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

    @PutMapping("/account/{id}/fixedshift")
    public ResponseEntity<String> putFixedShift(@PathVariable int id,
                                                @RequestParam(value = "date", defaultValue = "1900/01/01") Date date,
                                                @RequestParam(value = "shift", defaultValue = "1") int shift) {
        //todo maybe the defaultValue shouldn't be hard code
        if (shift > 4 || shift < 1) {
            // todo Should I use 'status.CONFLICT' ?
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Shift number should between 1 and 4");
        }
        return PutMethod.putFixedShift(id, date, shift);
    }

}
