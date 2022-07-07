package edu.uob.prototype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class BuildSchedule {
    private static LocalDate startDate;
    private static LocalDate endDate;
    private static int numberOfDoctors;
    private static int numberOfDays;
    private static int rulesBroken;
    private static ArrayList<JuniorDoctor> doctors;
    private static ArrayList<String> description;

    public BuildSchedule(LocalDate start, LocalDate end, int days, ArrayList<JuniorDoctor> doc){
        startDate = start;
        endDate = end;
        numberOfDays = days;
        numberOfDoctors = doc.size();
        doctors = doc;


        addShifts();
        Rules rulesBrokenCount = new Rules(doctors, startDate, endDate);
        rulesBroken = rulesBrokenCount.getRulesBroken();
        description = rulesBrokenCount.getDescriptions();

    }

    public int getRulesCount(){
        return rulesBroken;
    }

    public ArrayList<String> getDescription(){
        return description;
    }

    public ArrayList<JuniorDoctor> returnDoctors(){
        return doctors;
    }

    private static void addShifts(){

        int count = 0;
        while(true) {
            resetDoctors();
            checkStudyAndAnnualLeave();
            checkNotOnCall();
            painWeekCheck();
            if(selectWeekends()){
               if (calculateNightShifts()) {
                    if (addLongDayShifts()) {
                        reduceTheatreShifts();
                        if(addTheatreShifts()) {
                            setOffDays();
                            break;
                        }
                    }
                }
            }
            count++;
        }
    }


    private static void resetDoctors(){
        for(JuniorDoctor doctor : doctors){
            doctor.resetDoctor();
        }
    }

    private static void checkNotOnCall(){
        for(JuniorDoctor doctor : doctors){
            Hashtable<LocalDate, NotOnCallRequestType> notOnCall = doctor.getNotOnCallRequest();
            if(!notOnCall.isEmpty()){
                Set<LocalDate> setOfKeys = notOnCall.keySet();
                for (LocalDate date : setOfKeys) {
                    if(doctor.getNotOnCallRequestType(date).equals(NotOnCallRequestType.DAY)) {
                        doctor.setShifts(date, Shifts.NOCR);
                    }
                    //add else option for half days
                }
            }
        }
    }

    private static void checkStudyAndAnnualLeave(){
        for(JuniorDoctor doctor : doctors){
            Hashtable<LocalDate, LeaveType> leave = doctor.getAnnualOrStudyLeaveRequest();
            if(!leave.isEmpty()){
                Set<LocalDate> setOfKeys = leave.keySet();
                for (LocalDate date : setOfKeys) {
                    doctor.setShifts(date, Shifts.AorSL);
                    doctor.reduceTheatre();
                }
            }
        }
    }

    private static void painWeekCheck(){
        for(JuniorDoctor doctor : doctors){
            if(doctor.getPainWeek()){
                addPainWeek(doctor);
            }
        }
    }

    private static void addPainWeek(JuniorDoctor doctor){
        while(true){
            int daySelection = ThreadLocalRandom.current().nextInt(0, numberOfDays +1);
            LocalDate date = startDate.plusDays(daySelection);
            if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) && date.plusDays(8).isBefore(endDate)
                    && checkShiftFree(doctor,date, 8)){
                doctor.setPainWeekStartDate(date.plusDays(2));
                for(int i=0; i< 9; i++){
                    if(i<2 || i > 6) {
                        doctor.setShifts(date, Shifts.DAYOFF);
                    }else{
                        doctor.setShifts(date, Shifts.THEATRE);
                        doctor.reduceTheatre();
                    }
                    date = date.plusDays(1);
                }
                break;
            }
        }
    }

    private static boolean selectWeekends(){
        int counter = 0;
        LocalDate date = startDate;
        while (date.isBefore(endDate) && counter < 12){
            if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
                if(addWeekends(date, Shifts.NIGHT) && addWeekends(date, Shifts.DAYON)){
                    date = date.plusDays(1);
                    counter++;
                }else{
                    return false;
                }
            }
            date =date.plusDays(1);
        }

        return true;
    }

    private static boolean addWeekends(LocalDate date, Shifts shift){
        int errorCounter = 0;
        while(errorCounter < 50) {
            //chooses a random doctor
            int selectDoctor1 = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
            JuniorDoctor doctor = doctors.get(selectDoctor1);
            //if doctor has no more available weekends, it restarts the loop, selecting a new doctor
            if (doctor.getWeekends() == 0) {
                errorCounter++;
                continue;
            }
            if(shift.equals(Shifts.NIGHT)){
                if(!checkShiftFree(doctor, date, 6)){
                    errorCounter++;
                    continue;
                }
            }else{
                if(!checkShiftFree(doctor, date, 5)){
                    errorCounter++;
                    continue;
                }
            }
            //sets the weekend shifts
            for (int i=0; i<3; i++){
                doctor.setShifts(date, shift);
                date = date.plusDays(1);
            }
            //night shifts require 3 days off afterwards, day shifts only require 2 days off
            //reduced the number of remaining shifts for the doctor of that type
            int daysOff = 0;
            if (shift.equals(Shifts.NIGHT)) {
                doctor.reduceNights(3);
                doctor.reduceTotalOnCall(3);
                daysOff = 3;
            }else{
                daysOff = 2;
                doctor.reduceLongDays(3);
                doctor.reduceTotalOnCall(3);
            }
            //adds the off days
            for (int i=0; i<daysOff; i++){
                if (shift.equals(Shifts.NIGHT)) {
                    doctor.setShifts(date, Shifts.NAOFF);
                }else{
                    doctor.setShifts(date, Shifts.DAYOFF);
                }
                date = date.plusDays(1);
            }
            doctor.reduceWeekends();
            break;
        }
        return errorCounter != 50;
    }


    private static void reduceTheatreShifts(){
        for(JuniorDoctor doctor : doctors){
            int nights = doctor.getNights();
            while(nights < 0){
                doctor.reduceTheatre();
                nights++;
            }
            int days = doctor.getLongDays();
            while(days < 0){
                doctor.reduceTheatre();
                days++;
            }
        }
    }

    private static boolean calculateNightShifts(){
        int errorCounter = 0;
        LocalDate date = startDate;

        while (date.isBefore(endDate) && errorCounter < 500) {
            int shiftPairing = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
            JuniorDoctor doctor = doctors.get(shiftPairing);

            if(doctor.getNights() <= 0){
                continue;
            }

            if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                // add option for 4 nights in a row request
                if(checkShiftFree(doctor, date, 4) && addNightShifts(doctor, date)) {
                    date = date.plusDays(1);
                    continue;
                }
                else {
                    errorCounter++;
                    continue;
                }
            }
            else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
                if(checkShiftFree(doctor, date, 4) && addNightShifts(doctor, date)) {
                    date = date.plusDays(1);
                    continue;
                }else {
                    errorCounter++;
                    continue;
                }
            }
            else{
                date = date.plusDays(1);
            }
            errorCounter++;


        }
        return errorCounter != 500;
    }

    private static boolean addNightShifts(JuniorDoctor doctor, LocalDate shift){


        LocalDate date = shift;

        for (int i = 0; i< 2; i++){
            doctor.setShifts(date, Shifts.NIGHT);
            date = date.plusDays(1);
        }

        for (int i = 0; i< 2; i++){
            doctor.setShifts(date, Shifts.NAOFF);
            date = date.plusDays(1);
        }
        doctor.reduceNights(2);
        doctor.reduceTotalOnCall(2);

        return  true;
    }

    private static boolean checkShiftFree(JuniorDoctor doctor, LocalDate shift, int numberOfDays){
        for(int i=0; i<numberOfDays; i++){
            if(doctor.shiftTaken(shift)){
                return false;
            }
            shift = shift.plusDays(1);
        }
        return true;
    }

    private static void setOffDays(){
        for(JuniorDoctor doctor : doctors) {
            LocalDate date = startDate;
            while (!date.isEqual(endDate.plusDays(1))) {
                if (!doctor.shiftTaken(date)) {
                    doctor.setShifts(date, Shifts.DAYOFF);
                }
                date = date.plusDays(1);
            }
        }
    }

    private static boolean addLongDayShifts(){
        LocalDate date = startDate;
        int difference = numberOfDays - (numberOfDoctors * 11);
        int errorCounter = 0;

        while (date.isBefore(endDate.minusDays(difference)) && errorCounter < 500) {
            if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
                date = date.plusDays(3);
                continue;
            }
            int shiftPairing = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);

            JuniorDoctor doctor = doctors.get(shiftPairing);

            if(doctor.getLongDays() <= 0 || doctor.shiftTaken(date) || doctor.getTotalOnCall() <= 0){
                errorCounter++;
            }else {

                doctor.setShifts(date, Shifts.DAYON);
                doctor.reduceLongDays();
                doctor.reduceTotalOnCall();
                date = date.plusDays(1);
            }

        }
        if(errorCounter!=500){addExtraDays(difference, date);}
        return  errorCounter != 500;
    }

    private static void addExtraDays(int difference, LocalDate date){
        int index = 0;
        while(index<difference){
            int shiftPairing = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
            JuniorDoctor doctor = doctors.get(shiftPairing);
            if(!doctor.shiftTaken(date)) {
                doctor.setShifts(date, Shifts.DAYON);
                doctor.reduceLongDays();
                doctor.reduceTotalOnCall();
                date = date.plusDays(1);
                index++;
            }
        }
    }

    private static boolean addTheatreShifts(){
        int errorCounter;
        for(JuniorDoctor doctor : doctors){
            errorCounter = 0;
            while(doctor.getTheatre() > 0 && errorCounter < 500){
                int daySelection = ThreadLocalRandom.current().nextInt(0, numberOfDays +1);
                LocalDate date = startDate.plusDays(daySelection);
                if(!doctor.shiftTaken(date) && !date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                        && !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                    doctor.setShifts(date, Shifts.THEATRE);
                    doctor.reduceTheatre();
                }else{
                    errorCounter++;
                }
            }
            if(errorCounter == 500){
                return false;
            }
        }
        return true;

    }
}
