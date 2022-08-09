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

@CrossOrigin
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
                                                     @RequestParam boolean saturday, @RequestParam boolean sunday, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return PutOperations.putWorkingDays(accountId, monday, tuesday, wednesday, thursday, friday, saturday, sunday);
    }

    @GetMapping(value = "/notification", produces = "application/json")
    public ResponseEntity<ObjectNode> getNotifications(@RequestParam int accountId, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return GetOperations.getNotifications(accountId);
    }

    @GetMapping(value = "/leaves", produces = "application/json")
    public ResponseEntity<ObjectNode> getLeaves(@RequestParam int accountId, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return GetOperations.getLeaves(accountId);
    }

    @DeleteMapping(value = "/account/{accountId}", produces = "application/json")
    public ResponseEntity<ObjectNode> deleteAccount(@PathVariable int accountId, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 1); // Admin only request
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
                                                   @RequestParam(required = false) String fixedWorking,
                                                   @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return PatchOperations.patchAccount(accountId, annualLeave, studyLeave, workingHours, level, email,
                phone, doctorId, accountStatus, doctorStatus, timeWorked, fixedWorking);
    }

    @GetMapping(value = "/account/{accountId}", produces = "application/json")
    public ResponseEntity<ObjectNode> getAccount(@PathVariable int accountId, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return GetOperations.getAccount(accountId);
    }

    @GetMapping(value = "/account", produces = "application/json")
    public ResponseEntity<ObjectNode> getAllAccounts(@RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 1); // Admin only request
        return GetOperations.getAllAccounts();
    }

    @PatchMapping(value = "/notification/{notificationId}", produces = "application/json")
    public ResponseEntity<ObjectNode> patchNotification(@PathVariable int notificationId,
                                                        @RequestParam int accountId,
                                                        @RequestParam String status,
                                                        @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 1); // Admin only request
        return PatchOperations.patchNotification(notificationId, accountId, status);
    }

    @PostMapping(value = "/request/leave", produces = "application/json")
    public ResponseEntity<ObjectNode> postRequestLeave(@RequestParam int accountId, @RequestParam String date, @RequestParam int type,
                                                       @RequestParam int length, @RequestParam String note, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return PostOperations.postRequestLeave(accountId, date, type, length, note);

    }

    @PutMapping("/account/{accountId}/fixedshift")
    public ResponseEntity<ObjectNode> putFixedShift(@PathVariable int accountId,
                                                    @RequestParam Date date,
                                                    @RequestParam int shiftType,
                                                    @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return PutOperations.putFixedShift(accountId, date, shiftType);
    }

    @GetMapping(value = "/shift/{year}", produces = "application/json")
    public ResponseEntity<ObjectNode> getShifts(@PathVariable int year, @RequestParam int accountId, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return GetOperations.getShifts(year, accountId);
    }

    @PutMapping(value = "/rotabuild", produces = "application/json")
    public ResponseEntity<ObjectNode> putRotaBuild(@RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 1); // Admin only request
        //todo input full logic
        return okResponse("");
    }

    @GetMapping(value = "/login", produces = "application/json")
    public ResponseEntity<ObjectNode> getLogin(@RequestHeader String password, @RequestParam String username) {
        return GetOperations.getLogin(username, password);
    }

    @PostMapping(value = "/account", produces = "application/json")
    public ResponseEntity<ObjectNode> postAccount(@RequestParam String username, String email, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 1); // Admin only request
        return PostOperations.postAccount(username, email);
    }

    @PatchMapping(value = "/password", produces = "application/json")
    public ResponseEntity<ObjectNode> patchPassword(@RequestHeader String oldPassword, @RequestHeader String newPassword,
                                                    @RequestParam int accountId, @RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return PatchOperations.patchPassword(oldPassword, newPassword, accountId);
    }

    @PatchMapping(value = "/passwordReset", produces = "application/json")
    public ResponseEntity<ObjectNode> patchPasswordReset(@RequestParam String username, @RequestParam String email) {
        return PatchOperations.patchPasswordReset(username, email);
    }

    @PatchMapping(value = "/logout", produces = "application/json")
    public ResponseEntity<ObjectNode> patchLogout(@RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 0);
        return PatchOperations.patchLogout(token);
    }

    @PostMapping(value = "/rotagroup", produces = "application/json")
    public ResponseEntity<ObjectNode> postRotaGroup(@RequestHeader String token, @RequestParam String startDate,
                                                    @RequestParam String endDate) {
        ConnectionTools.validTokenAuthorised(token, 1); // Admin only request
        return PostOperations.postRotaGroup(startDate, endDate);
    }

    @GetMapping(value = "/rotagroup", produces = "application/json")
    public ResponseEntity<ObjectNode> postRotaGroup(@RequestHeader String token) {
        ConnectionTools.validTokenAuthorised(token, 1); // Admin only request
        return GetOperations.getRotaGroup();
    }
    
    @PutMapping(value = "/account/{{accountId}/rotatype", produces = "application/json")
    public ResponseEntity<ObjectNode> putAccountRotaType(@RequestHeader String token, @PathVariable int accountId,
                                                         @RequestParam int rotaTypeId,
                                                         @RequestParam String startDate, @RequestParam String endDate) {
        ConnectionTools.validTokenAuthorised(token, 1); // Admin only request
        return PutOperations.putAccountRotaType(accountId, rotaTypeId, startDate, endDate);
    }
}
