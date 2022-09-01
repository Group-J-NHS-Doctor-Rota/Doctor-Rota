package edu.uob.RotaBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.uob.BuildRota;
import org.springframework.http.ResponseEntity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;


public class SortData {

    private static LocalDate startDate;
    private static LocalDate endDate;
    private static int leave;

    public static void SortData(JsonNode rootNode, ArrayList<Date> dates) throws JsonProcessingException {


        startDate = convertToLocalDateViaSqlDate(dates.get(0));
        endDate = convertToLocalDateViaSqlDate(dates.get(1));


        ArrayList<ArrayList<JuniorDoctor>> allDoctors = new ArrayList<>();
        ArrayList<Hashtable<LocalDate, ArrayList<Shifts>>> allFixedWorking = new ArrayList<>();

        for(int j=1; j<5; j++) {

                ArrayList<String> ids = new ArrayList<>();
                ArrayList<Double> hours = new ArrayList<>();
                ArrayList<Boolean> painWeek = new ArrayList<>();
                Hashtable<LocalDate, ArrayList<Shifts>> fixedWorkingPattern = new Hashtable<>();
                ArrayList<LocalDate> startDates = new ArrayList<>();
                ArrayList<LocalDate> endDates = new ArrayList<>();

                int counter = 0;
                for (int i = 0; i < rootNode.get("accounts").size(); i++) {
                    JsonNode account = rootNode.get("accounts").get(i);
                    int index = Integer.parseInt(account.get("rotaTypeId").toString());
                    if (index == j && !ids.contains((account.get("id").toString())) &&
                            !account.get("fixedWorking").asBoolean()) {
                        ids.add((account.get("id").toString()));
                        ObjectMapper mapper = new ObjectMapper();
                        double hour = mapper.treeToValue(account.get("timeWorked"), Double.class);
                        hours.add(hour);
                        String startDate = account.get("startDate").toString();
                        startDate = startDate.replace("\"", "");
                        LocalDate date1 = LocalDate.parse(startDate);
                        startDates.add(date1);

                        String endDate = account.get("endDate").toString();
                        endDate = endDate.replace("\"", "");
                        LocalDate date2 = LocalDate.parse(endDate);
                        endDates.add(date2);


                        if (account.get("painWeek").asBoolean()) {
                            painWeek.add(true);
                        }else {
                            painWeek.add(false);
                        }
                    } else if (index == j && account.get("fixedWorking").asBoolean()) {
                        String dateString = account.get("fixedWorkingDate").toString();
                        dateString = dateString.replace("\"", "");
                        LocalDate date = LocalDate.parse(dateString);
                        Shifts shift = null;
                        if (account.get("fixedWorkingShift").asInt() == 1) {
                            counter++;
                            shift = Shifts.DAYON;
                        } else if(account.get("fixedWorkingShift").asInt() == 2){
                            counter++;
                            shift = Shifts.NIGHT;
                        }

                        if (shift != null && !fixedWorkingPattern.containsKey(date)) {
                            ArrayList<Shifts> shiftType = new ArrayList<>();
                            shiftType.add(shift);
                            fixedWorkingPattern.put(date, shiftType);
                        } else if (shift != null) {
                            ArrayList<Shifts> shiftTypes = fixedWorkingPattern.get(date);
                            shiftTypes.add(shift);
                            fixedWorkingPattern.put(date, shiftTypes);
                        }
                    }
                }
                allFixedWorking.add(fixedWorkingPattern);
                ArrayList<JuniorDoctor> doctors = addDoctorsData(ids, hours, painWeek, startDates, endDates, j);
                addLeave(doctors, rootNode, j);
                allDoctors.add(doctors);


        }

        deleteOldRotaData(allDoctors);


            int numberOfDays = setNumberOfDays(startDate, endDate);

//            ArrayList<String> rulesBrokenDescription = new ArrayList<>();

            ArrayList<JuniorDoctor> doctorsLeastErrors = new ArrayList<>();
            int lowestRulesBroken = 0;

            for (int i = 0; i < 4; i++) {
                ArrayList<JuniorDoctor> doctors = allDoctors.get(i);
                Hashtable<LocalDate, ArrayList<Shifts>> fixedWorking = allFixedWorking.get(i);

                int rulesBroken = 0;
                int counter = 0;
                do {
                    BuildSchedule iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors, fixedWorking);
                    rulesBroken = iteration.getRulesCount();
                    if (counter == 0) {
                        doctorsLeastErrors = copyDoctors(doctors);
                        lowestRulesBroken = rulesBroken;
//                        rulesBrokenDescription = iteration.getDescriptions();
                    } else {
                        if (rulesBroken < lowestRulesBroken) {
                            doctorsLeastErrors = copyDoctors(doctors);
                            lowestRulesBroken = rulesBroken;
//                            rulesBrokenDescription = iteration.getDescriptions();
                        }
                    }
                    counter++;
                } while (rulesBroken > 0 && counter < 5000);

                addNewData(doctorsLeastErrors);

            }




    }

    private static void deleteOldRotaData(ArrayList<ArrayList<JuniorDoctor>> allDoctors){

        Date start = convertToDateViaSqlDate(startDate);
        Date end = convertToDateViaSqlDate(endDate);

        for(ArrayList<JuniorDoctor> doctors : allDoctors){
            for (JuniorDoctor doctor : doctors) {
                BuildRota.deleteOldRotaData(Integer.parseInt(doctor.getName()), start, end);
            }
        }

    }

    private static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    private static void addNewData(ArrayList<JuniorDoctor> doctors){

        int shiftType = 0;

        for (JuniorDoctor doctor : doctors) {
            LocalDate date = startDate;
            while (!date.isEqual(endDate.plusDays(1))) {
                if(date.isBefore(doctor.getEndDate()) && date.isAfter(doctor.getStartDate())) {
                    Shifts shift = doctor.getShiftType(date);
                    if (doctor.onShift(date)) {
                        if (shift.equals(Shifts.THEATRE)) {
                            shiftType = 0;
                        } else if (shift.equals(Shifts.DAYON)) {
                            shiftType = 1;
                        } else if (shift.equals(Shifts.NIGHT)) {
                            shiftType = 2;
                        }
                        BuildRota.addNewShifts(Integer.parseInt(doctor.getName()), convertToDateViaSqlDate(date), shiftType, 1, doctor.getRotaGroupId());
                    } else if (shift.equals(Shifts.DAOFF) || shift.equals(Shifts.NAOFF)) {
                        BuildRota.addNewShifts(Integer.parseInt(doctor.getName()), convertToDateViaSqlDate(date), 3, 1, doctor.getRotaGroupId());
                    }
                }
                date = date.plusDays(1);
            }
        }

    }

    public static ArrayList<JuniorDoctor> addDoctors(String[] names, double[] hours){
        ArrayList<JuniorDoctor> doctors = new ArrayList<>();
        for (int i=0; i<names.length; i++) {
            JuniorDoctor doctor = new JuniorDoctor(hours[i]);
            doctor.setName(names[i]);
            doctors.add(doctor);
        }
        return doctors;
    }


    private static int acrossFour(ArrayList<ArrayList<JuniorDoctor>> allDoctors){

        ArrayList<Hashtable<LocalDate, Integer>> allShifts = new ArrayList<>();

        for(ArrayList<JuniorDoctor> doctors : allDoctors){
            Hashtable<LocalDate, Integer> shifts = new Hashtable<>();
            LocalDate date = startDate;
            while (!date.isEqual(endDate.plusDays(1))) {
                int counter = 0;
                for (JuniorDoctor doctor : doctors) {
                    if (!doctor.onShift(date)) {
                        counter++;
                    }
                }
                shifts.put(date, counter);
                date = date.plusDays(1);
            }
            allShifts.add(shifts);
        }

        return addUpAcrossFour(allShifts);


    }


    private static int addUpAcrossFour(ArrayList<Hashtable<LocalDate, Integer>> allShifts){

        LocalDate date = startDate;
        int counter = 0;

        while (!date.isEqual(endDate.plusDays(1))) {
            if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                date = date.plusDays(1);
                continue;
            }
            int first = allShifts.get(0).get(date) + allShifts.get(1).get(date);
            if (first >= 6) {
                counter++;
            }
            int second = allShifts.get(2).get(date) + allShifts.get(3).get(date);
            if (second >= 6) {
                counter++;
            }
            if ((first + second) >= 12) {
                counter++;
            }
            date = date.plusDays(1);
        }
        return counter;

    }

    public static LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public static ArrayList<JuniorDoctor> copyDoctors(ArrayList<JuniorDoctor> doctors){
        ArrayList<JuniorDoctor> newDoctors = new ArrayList<>();
        for(JuniorDoctor doc : doctors){
            Hashtable<LocalDate, Shifts> shifts = doc.returnAllShifts();
            JuniorDoctor a = new JuniorDoctor(doc.getHours());
            a.resetDoctor();
            a.setAllShifts(shifts);
            a.setName(doc.getName());
            a.setStartDate(doc.getStartDate());
            a.setEndDate(doc.getEndDate());
            a.setRotaGroupId(doc.getRotaGroupId());
            newDoctors.add(a);
        }
        return newDoctors;
    }


    public static ArrayList<JuniorDoctor> addDoctorsData(ArrayList<String> ids, ArrayList<Double> hours, ArrayList<Boolean> painWeek,
                                                         ArrayList<LocalDate> startDates, ArrayList<LocalDate> endDates, int rotaGroupId){
        ArrayList<JuniorDoctor> doctors = new ArrayList<>();
        for (int i=0; i<ids.size(); i++) {
            JuniorDoctor doctor = new JuniorDoctor(hours.get(i));
            doctor.setName(ids.get(i));
            doctors.add(doctor);
            if(painWeek.get(i)){
                doctor.setPainWeek();
            }
            doctor.setStartDate(startDates.get(i));
            doctor.setEndDate(endDates.get(i));
            doctor.setRotaGroupId(rotaGroupId);

        }
        return doctors;
    }

    public static void addLeave(ArrayList<JuniorDoctor> doctors, JsonNode rootNode, int index){
        for(JuniorDoctor doctor : doctors) {
            for (int i = 0; i < rootNode.get("accounts").size(); i++) {
                JsonNode account = rootNode.get("accounts").get(i);
                int rotaId = Integer.parseInt(account.get("rotaTypeId").toString());
                if(rotaId == index && !account.get("leaveDate").toString().equals("null") &&
                        account.get("leaveStatus").asInt() == 1 && doctor.getName().equals(account.get("id").toString())){
                    String dateString = account.get("leaveDate").toString();
                    dateString = dateString.replace("\"", "");
                    LocalDate date = LocalDate.parse(dateString);
                    if(account.get("leaveType").asInt() == 0){
                        doctor.addAnnualOrStudyLeaveRequest(date, LeaveType.ANNUAL);
                        leave++;
                    }
                    else if(account.get("leaveType").asInt() == 1){
                        doctor.addAnnualOrStudyLeaveRequest(date, LeaveType.STUDY);
                        leave++;
                    }
                    else{
                        doctor.addNotOnCallRequest(date, NotOnCallRequestType.DAY);
                        leave++;
                    }

                }
            }
        }
    }



    public static int setNumberOfDays(LocalDate startDate, LocalDate endDate){
        LocalDate date = startDate;
        int counter = 0;
        while (!date.isEqual(endDate)) {
            date = date.plusDays(1);
            counter++;
        }
        return counter;
    }

}
