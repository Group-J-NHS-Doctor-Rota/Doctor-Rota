package edu.uob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class BuildRota {

    public static ResponseEntity<ObjectNode> buildRota(int rotaGroupId){
        ArrayList<Date> dates = getStartAndEndDate(rotaGroupId);
        if(dates.size() == 2){
            getJuniorDoctorsData(dates);
        }
        else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
    }

    public static ResponseEntity<ObjectNode> getJuniorDoctorsData(ArrayList<Date> dates){

        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            // Get all accounts and store in list

            String SQL = "SELECT R.accountId, R.rotaTypeId, R. startDate, R.endDate, " +
                    "A.timeWorked, A.fixedWorking, " +
                    "L.date AS leaveDate, L.type, L.length, L.status " +
                    ", F.date, F.shiftType " +
                    "FROM accountRotaTypes R " +
                    "INNER JOIN accounts A ON A.id = R.accountId " +
                    "LEFT JOIN leaveRequests L ON L.accountId = R.accountId " +
                    "LEFT JOIN fixedRotaShifts F ON F.accountId = R.accountId " +
                    "WHERE (R.startDate <= ? OR R.startDate BETWEEN ? AND ?) AND " +
                    "(R.endDate BETWEEN ? AND ? OR R.endDate >= ?); ";

            try(PreparedStatement s = c.prepareStatement(SQL)) {
                s.setDate(1, (java.sql.Date) dates.get(0));
                s.setDate(2, (java.sql.Date) dates.get(0));
                s.setDate(3, (java.sql.Date) dates.get(1));
                s.setDate(4, (java.sql.Date) dates.get(0));
                s.setDate(5, (java.sql.Date) dates.get(1));
                s.setDate(6, (java.sql.Date) dates.get(1));
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                ArrayNode arrayNode = objectNode.putArray("accounts");
                ResultSet r = s.executeQuery();
                while(r.next()) {
                    ObjectNode objectNodeRow = new ObjectMapper().createObjectNode();
                    addAccountDetailsToObjectNode(r, objectNodeRow);
                    arrayNode.add(objectNodeRow);
                }
                return ResponseEntity.status(HttpStatus.OK).body(objectNode);
            }
            // Have to catch SQLException exception here
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }

    }

    public static ArrayList<Date> getStartAndEndDate(int id){
        String connectionString = ConnectionTools.getConnectionString();
        try(Connection c = DriverManager.getConnection(connectionString)) {
            String SQL = "SELECT * FROM rotaGroups WHERE id = ?;";
            try (PreparedStatement s = c.prepareStatement(SQL)) {
                s.setInt(1, id);
                ObjectNode objectNode = new ObjectMapper().createObjectNode();
                ResultSet r = s.executeQuery();
                ArrayList<java.util.Date> startAndEnd = new ArrayList<>();
                if(r.next()) {
                    startAndEnd.add(r.getDate("startDate"));
                    startAndEnd.add(r.getDate("endDate"));
                }
                return startAndEnd;
            }
        } catch (SQLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.toString());
        }
    }

    private static void addAccountDetailsToObjectNode(ResultSet r, ObjectNode objectNode) throws SQLException {
        objectNode.put("id", r.getInt("accountId"));
        objectNode.put("rotaTypeId", r.getInt("rotaTypeId"));
        objectNode.put("startDate", String.valueOf(r.getDate("startDate")));
        objectNode.put("endDate", String.valueOf(r.getDate("endDate")));
        objectNode.put("leaveDate", r.getString("leaveDate"));
        objectNode.put("leaveType", r.getString("type"));
        objectNode.put("leaveLength", r.getString("length"));
        objectNode.put("leaveStatus", r.getString("status"));
        objectNode.put("timeWorked", r.getFloat("timeWorked"));
        objectNode.put("fixedWorking", r.getBoolean("fixedWorking"));

    }
}
