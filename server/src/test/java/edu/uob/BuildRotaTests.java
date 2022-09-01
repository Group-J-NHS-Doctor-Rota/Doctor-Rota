package edu.uob;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class BuildRotaTests {
    ArrayList<Date> dates = new ArrayList<>();

    @Test
    void testGetConnectionString() {
        String connectionString = ConnectionTools.getConnectionString();
        // Check connection sting has been found, so is not empty or null
        assertNotEquals("", connectionString, "Connection string shouldn't be empty");
        assertNotEquals(null, connectionString, "Connection string shouldn't be null");
    }

    @Test
    void getNewDate() throws JsonProcessingException {

        dates = BuildRota.getStartAndEndDate(1);

        Date start = dates.get(0);
        Date end = dates.get(1);

        ResponseEntity<ObjectNode> response = BuildRota.getData(start, end);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));

        int numberOfAccounts = rootNode.get("accounts").size();

        for(int i = 0; i < numberOfAccounts; i++) {
            JsonNode account = rootNode.get("accounts").get(i);
            assertTrue(account.has("id"));
            assertTrue(account.has("rotaGroupId"));
            assertTrue(account.has("rotaTypeId"));
            assertTrue(account.has("date"));
            assertTrue(account.has("shiftType"));
        }


    }

    private Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    @Test
    void getRotaData() throws JsonProcessingException {
        dates = BuildRota.getStartAndEndDate(1);
        ResponseEntity<ObjectNode> response = BuildRota.getJuniorDoctorsData(dates);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(String.valueOf(response.getBody()));

        int numberOfAccounts = rootNode.get("accounts").size();

        ArrayList<Integer> found = new ArrayList<>();


        // Check all accounts, have all the fields
        for(int i = 0; i < numberOfAccounts; i++) {
            JsonNode account = rootNode.get("accounts").get(i);
            int index = Integer.parseInt(account.get("id").toString());
            if(!found.contains(index-1)){
                found.add(index-1);
            }
            assertTrue(account.has("id"));
            assertTrue(account.has("rotaTypeId"));
            assertTrue(account.has("startDate"));
            assertTrue(account.has("endDate"));
            assertTrue(account.has("timeWorked"));
            assertTrue(account.has("fixedWorking"));
            assertTrue(account.has("leaveDate"));
            assertTrue(account.has("leaveType"));
            assertTrue(account.has("leaveLength"));
            assertTrue(account.has("leaveStatus"));
            assertTrue(account.has("fixedWorkingDate"));
            assertTrue(account.has("fixedWorkingShift"));
            assertTrue(account.has("painWeek"));

        }

        for(int i=0; i< 40; i++){
                assertTrue(found.contains(i));
        }
    }

    //don't want to run this with every test as it rebuilds the rota

//    @Test
//    void buildRota() throws JsonProcessingException {
//        BuildRota.buildRota(1);
//    }

}
