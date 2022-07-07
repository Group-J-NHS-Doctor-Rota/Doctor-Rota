package edu.uob.prototype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Rules {
    private static LocalDate startDate;
    private static LocalDate endDate;
    private static int rulesBroken;
    private static ArrayList<JuniorDoctor> doctors;
    private static ArrayList<String> rulesBrokenDescription;

    public Rules(ArrayList<JuniorDoctor> doctorsList, LocalDate start, LocalDate end){
        startDate = start;
        endDate = end;
        rulesBroken = 0;
        rulesBrokenDescription = new ArrayList<>();
        doctors = doctorsList;

        for(JuniorDoctor doctor : doctorsList){
            fourLongDaysInARow(doctor);
            sevenConsecutiveShifts(doctor);
            threeWeekendsInARow(doctor);
            seventyTwoHourWorkWeek(doctor);
        }

    }

    private static void fourLongDaysInARow(JuniorDoctor doctor){
        LocalDate date = startDate;
        int counter = 0;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(counter > 4){
                rulesBroken++;
                rulesBrokenDescription.add("Four long days in a row -> " + doctor.getName() +" = " + date.minusDays(4) + " - " + date);
            }
            if(counter == 4){
                check48Hours(date, doctor);
            }
            if(doctor.getShiftType(date) == Shifts.DAYON){
                counter++;
            }else {
                counter = 0;
            }
            date = date.plusDays(1);
        }
    }

    private static void sevenConsecutiveShifts(JuniorDoctor doctor){
        LocalDate date = startDate;
        int counter = 0;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(counter > 7){
                rulesBroken++;
                rulesBrokenDescription.add("seven shifts in a row -> " + doctor.getName()
                        +" = " + date.minusDays(7) + " - " + date);
            }
            if(counter == 7){
                check48Hours(date, doctor);
            }
            if(doctor.getShiftType(date) == Shifts.DAYON || doctor.getShiftType(date) == Shifts.NIGHT ||
                    doctor.getShiftType(date) == Shifts.THEATRE){
                counter++;
            }else {
                counter = 0;
            }
            date = date.plusDays(1);
        }
    }

    public static void check48Hours(LocalDate date, JuniorDoctor doctor) {
        int counter = 0;
        date = date.plusDays(1);
        for (int i = 0; i < 2; i++) {
            if (doctor.getShiftType(date) == Shifts.DAYON || doctor.getShiftType(date) == Shifts.NIGHT ||
                    doctor.getShiftType(date) == Shifts.THEATRE) {
                counter++;
            }
            date = date.plusDays(1);
        }
        if(counter == 2){
            rulesBroken++;
        }
    }

    public static void threeWeekendsInARow(JuniorDoctor doctor){
        LocalDate date = startDate;
        int totalWeekends = 0;
        int weekendOnCounter = 0;
        int weekendOffCounter = 0;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                if(doctor.onShift(date)){
                    weekendOnCounter++;
                }
                else{
                    weekendOffCounter++;
                }
            }
            if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                if (weekendOnCounter > 0){
                    totalWeekends++;
                }else if(weekendOffCounter == 2){
                    totalWeekends = 0;
                }
                if(totalWeekends == 3){
                    //return false;
                    rulesBroken++;
                    rulesBrokenDescription.add("three weekends in a row -> " + doctor.getName());
                }
                weekendOffCounter = 0;
                weekendOnCounter = 0;
            }

            date = date.plusDays(1);
        }
    }


    private static void seventyTwoHourWorkWeek(JuniorDoctor doctor){
        LocalDate date = startDate;
        double hoursWorking = 0;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(date.getDayOfWeek().equals(DayOfWeek.TUESDAY)){
                if(hoursWorking > 72){
                    rulesBroken++;
                    rulesBrokenDescription.add("more than 72 hours in a week-> " + doctor.getName() + " - " +
                            date.minusDays(7) +" to "+ date);
                }
            }else if(date.getDayOfWeek().equals(DayOfWeek.WEDNESDAY)){
                hoursWorking = 0;
            }
            if(doctor.getShiftType(date) == Shifts.DAYON || doctor.getShiftType(date) == Shifts.NIGHT){
                hoursWorking = hoursWorking + 12.5;
            }else if(doctor.getShiftType(date) == Shifts.THEATRE){
                hoursWorking = hoursWorking + 10;
            }
            date = date.plusDays(1);
        }
    }

    public int getRulesBroken(){
        return rulesBroken;
    }

    public ArrayList<String> getDescriptions(){
        return rulesBrokenDescription;
    }
}
