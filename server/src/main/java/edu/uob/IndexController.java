package edu.uob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Date;
import java.sql.Timestamp;

@RestController
public class IndexController {

    public static ResponseEntity<ObjectNode> okResponse(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("message", message);
        objectNode.put("timestamp", String.valueOf(new Timestamp(System.currentTimeMillis())));
        return ResponseEntity.status(HttpStatus.OK).body(objectNode);
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<ObjectNode> index() {
        return okResponse("Spring boot server running correctly");
    }

    @GetMapping(value = "/test", produces = "application/json")
    public ResponseEntity<ObjectNode> getTest() {
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            return okResponse("Spring boot server and sql database running correctly");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    e.toString().concat("\nSpring boot server and/or sql database not running correctly"));
        }
    }

    @PutMapping(value = "/account/{accountId}/workingdays", produces = "application/json")
    public ResponseEntity<ObjectNode> putWorkingDays(@PathVariable int accountId, @RequestParam boolean monday, @RequestParam boolean tuesday,
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

    @GetMapping(value = "/leaves", produces = "application/json")
    public ResponseEntity<ObjectNode> getLeaves(@RequestParam int accountId) {
        return GetOperations.getLeaves(accountId);
    }

    @DeleteMapping(value = "/account/{accountId}", produces = "application/json")
    public ResponseEntity<ObjectNode> deleteAccount(@PathVariable int accountId) {
        //todo check token is valid
        return DeleteOperations.deleteAccount(accountId);
    }

    @PatchMapping(value = "/account/{accountId}", produces = "application/json")
    // Optional request parameters can't be primitives and no null value
    // Better to have them as String and convert later
    public ResponseEntity<ObjectNode> patchAccount(@PathVariable int accountId, @RequestParam(required = false) String annualLeave,
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
    public ResponseEntity<ObjectNode> patchNotification(@PathVariable int notificationId,
                                                    @RequestParam int accountId,
                                                    @RequestParam String status) {
        return PatchOperations.patchNotification(notificationId, accountId, status);
    }

    @PostMapping(value = "/request/leave", produces = "application/json")
    public ResponseEntity<ObjectNode> postRequestLeave(@RequestParam int accountId, @RequestParam String date, @RequestParam int type,
                                                   @RequestParam int length, @RequestParam String note) {
        //todo check token is valid
        return PostOperations.postRequestLeave(accountId, date, type, length, note);

    }

    @PutMapping("/account/{accountId}/fixedshift")
    public ResponseEntity<ObjectNode> putFixedShift(@PathVariable int accountId,
                                                @RequestParam Date date,
                                                @RequestParam int shiftType) {
        return PutOperations.putFixedShift(accountId, date, shiftType);
    }

    @GetMapping(value = "/shift/{year}", produces = "application/json")
    public ResponseEntity<ObjectNode> getShifts(@PathVariable int year, @RequestParam int accountId) {
        return GetOperations.getShifts(year, accountId);
    }

    @PutMapping(value = "/rotabuild", produces = "application/json")
    public ResponseEntity<ObjectNode> putRotaBuild() {
        //todo check token is valid
        //todo input full logic
        return okResponse("");
    }

    @GetMapping(value = "/login", produces = "application/json")
    public ResponseEntity<ObjectNode> getLogin(@RequestHeader String password, @RequestParam String username) {
        //todo input full logic
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("token", "123someDudToken456");
        objectNode.put("accountId", 1);
        objectNode.put("level", 1);
        return ResponseEntity.status(HttpStatus.OK).body(objectNode);
    }

    @PutMapping(value = "/account", produces = "application/json")
    public ResponseEntity<ObjectNode> putAccount(@RequestParam String username) {
        //todo check token is valid
        //todo input full logic
        return okResponse("");
    }

    @PatchMapping(value = "/password", produces = "application/json")
    public ResponseEntity<ObjectNode> patchPassword(@RequestHeader String oldPassword, @RequestHeader String newPassword,
                                                @RequestParam String accountId) {
        //todo check token is valid
        //todo input full logic
        if(oldPassword.equals(newPassword)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "New password cannot be the same as the old password!\n");
        }
        return okResponse("");
    }

    @PatchMapping(value = "/passwordReset", produces = "application/json")
    public ResponseEntity<ObjectNode> patchPasswordReset(@RequestParam String username, @RequestParam String email) {
        //todo input full logic
        return okResponse("");
    }

    @PatchMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<ObjectNode> patchLogout() {
        //todo check token is valid
        //todo input full logic
        return okResponse("");
    }

}
