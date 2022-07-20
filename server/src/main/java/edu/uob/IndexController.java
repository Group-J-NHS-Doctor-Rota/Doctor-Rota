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

    @PutMapping(value = "/account/{accountId}/workingdays", produces = "application/json")
    public ResponseEntity<String> putWorkingDays(@PathVariable int accountId, @RequestParam boolean monday, @RequestParam boolean tuesday,
                                                 @RequestParam boolean wednesday, @RequestParam boolean thursday, @RequestParam boolean friday,
                                                 @RequestParam boolean saturday, @RequestParam boolean sunday) {
        //todo check token is valid
        return PutOperations.putWorkingDays(accountId, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @GetMapping(value = "/notification", produces = "application/json")
    public ResponseEntity<ObjectNode> getNotifications(@RequestParam int accountId) {
        //todo check token is valid
        return GetOperations.getNotifications(accountId);
    }

    @DeleteMapping(value = "/account/{accountId}", produces = "application/json")
    public ResponseEntity<String> deleteAccount(@PathVariable int accountId) {
        //todo check token is valid
        return DeleteOperations.deleteAccount(accountId);
    }

    @PatchMapping(value = "/account/{accountId}", produces = "application/json")
    // Optional request parameters can't be primitives and no null value
    // Better to have them as String and convert later
    public ResponseEntity<String> patchAccount(@PathVariable int accountId, @RequestParam(required = false) String annualLeave,
                                               @RequestParam(required = false) String studyLeave,
                                               @RequestParam(required = false) String workingHours,
                                               @RequestParam(required = false) String level,
                                               @RequestParam(required = false) String email,
                                               @RequestParam(required = false) String phone,
                                               @RequestParam(required = false) String doctorId,
                                               @RequestParam(required = false) String accountStatus,
                                               @RequestParam(required = false) String doctorStatus,
                                               @RequestParam(required = false) String timeWorked,
                                               @RequestParam(required = false) String fixedWorking) {
        //todo check token is valid
        return PatchOperations.patchAccount(accountId, annualLeave, studyLeave, workingHours, level, email,
                phone, doctorId, accountStatus, doctorStatus, timeWorked, fixedWorking);
    }

    @GetMapping(value = "/account/{accountId}", produces = "application/json")
    public ResponseEntity<ObjectNode> getAccount(@PathVariable int accountId) {
        //todo check token is valid
        return GetOperations.getAccount(accountId);
    }

    @GetMapping(value = "/account", produces = "application/json")
    public ResponseEntity<ObjectNode> getAllAccounts() {
        //todo check token is valid
        return GetOperations.getAllAccounts();
    }

    @PatchMapping(value = "/notification/{notificationId}", produces = "application/json")
    // Optional request parameters can't be primitives and no null value
    // Better to have them as String and convert later
    public ResponseEntity<String> patchNotification(@PathVariable int notificationId,
                                                    @RequestParam int accountId,
                                                    @RequestParam String status) {
        return PatchOperations.patchNotification(notificationId, accountId, status);
    }

    @PostMapping(value = "/request/leave", produces = "application/json")
    public ResponseEntity<String> postRequestLeave(@RequestParam int accountId, @RequestParam String date, @RequestParam int type,
                                                   @RequestParam int length, @RequestParam String note) {
        //todo check token is valid
        return PostOperations.postRequestLeave(accountId, date, type, length, note);

    }

}
