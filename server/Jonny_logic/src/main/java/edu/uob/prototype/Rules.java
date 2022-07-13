package edu.uob.prototype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Rules {
    private static LocalDate startDate;
    private static LocalDate endDate;
    private static int rulesBroken;
    private static ArrayList<JuniorDoctor> doctors;
    private static int numberOfDays;
    private static boolean swaps;


    public Rules(ArrayList<JuniorDoctor> doctorsList, LocalDate start, LocalDate end){
        startDate = start;
        endDate = end;
        doctors = doctorsList;
        numberOfDays = setNumberOfDays(startDate, endDate);

        rulesBroken(doctorsList, start, end);

    }

    private static void rulesBroken(ArrayList<JuniorDoctor> doctors, LocalDate start, LocalDate end){
        rulesBroken = 0;
        for(JuniorDoctor doctor : doctors){
            rulesBroken += fourLongDaysInARow(doctor, start, end);
            rulesBroken += sevenConsecutiveShifts(doctor, start, end);
            rulesBroken += threeWeekendsInARow(doctor, start, end);
            rulesBroken += checkShiftCount(doctor);
        }

        if(rulesBroken == 0){
            for(JuniorDoctor doctor : doctors) {
                swaps = false;
                int counter = seventyTwoHourWorkWeek(doctor, start, end);
                if(swaps && counter == 0){
                    rulesBroken(doctors, start, end);
                }else{
                    rulesBroken += counter;
                }
            }
        }
        for (JuniorDoctor doctor: doctors){
            if(doctor.getPainWeek()) {
                rulesBroken += painWeek(doctor);
            }
        }
    }

    public static int painWeek(JuniorDoctor doctor){
        int errors = 0;
        LocalDate start = doctor.getPainWeekStartDate();
        start = start.minusDays(2);
        for(int i=0; i< 9; i++){
            if(i<2 || i > 6) {
                if(!doctor.getShiftType(start).equals(Shifts.DAYOFF)){
                    errors++;
                }
            }else{
                if(!doctor.getShiftType(start).equals(Shifts.THEATRE)){
                    errors++;
                }
            }
            start = start.plusDays(1);
        }
        return errors;
    }

    public static int fourLongDaysInARow(JuniorDoctor doctor, LocalDate startDate, LocalDate endDate){
        int errorCounter = 0;
        LocalDate date = startDate;
        int counter = 0;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(counter > 4){
                errorCounter++;
            }
            if(counter == 4){
                errorCounter += check48Hours(date, doctor);
            }
            if(doctor.getShiftType(date) == Shifts.DAYON){
                counter++;
            }else {
                counter = 0;
            }
            date = date.plusDays(1);
        }
        return errorCounter;
    }

    public static int sevenConsecutiveShifts(JuniorDoctor doctor, LocalDate startDate, LocalDate endDate){
        int errorCounter = 0;
        LocalDate date = startDate;
        int counter = 0;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(counter == 7){
                errorCounter += check48Hours(date, doctor);
                counter = 0;
            }
            if(doctor.getShiftType(date) == Shifts.DAYON || doctor.getShiftType(date) == Shifts.NIGHT ||
                    doctor.getShiftType(date) == Shifts.THEATRE){
                counter++;
            }else {
                counter = 0;
            }
            date = date.plusDays(1);
        }
        return errorCounter;
    }

    public static int check48Hours(LocalDate date, JuniorDoctor doctor) {
        int counter = 0;
        for (int i = 0; i < 2; i++) {
            if (doctor.getShiftType(date) == Shifts.DAYON || doctor.getShiftType(date) == Shifts.NIGHT ||
                    doctor.getShiftType(date) == Shifts.THEATRE) {
                counter++;
            }
            date = date.plusDays(1);
        }
        if(counter > 0){
            return 1;
        }
        return 0;
    }

    public static int threeWeekendsInARow(JuniorDoctor doctor, LocalDate startDate, LocalDate endDate){
        int errorCounter = 0;
        LocalDate date = startDate;
        int totalWeekends = 0;
        int weekendOnCounter = 0;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                if(doctor.onShift(date)){
                    weekendOnCounter++;
                }
            }
            if(date.getDayOfWeek().equals(DayOfWeek.MONDAY)){
                if (weekendOnCounter > 0){
                    totalWeekends++;
                }else if(weekendOnCounter == 0){
                    totalWeekends = 0;
                }
                if(totalWeekends >= 3){
                    errorCounter++;
                }
                weekendOnCounter = 0;
            }

            date = date.plusDays(1);
        }
        return errorCounter;
    }


    public static int seventyTwoHourWorkWeek(JuniorDoctor doctor, LocalDate startDate, LocalDate endDate){
        ArrayList<Double> hours = new ArrayList<>();

        int errorCounter = 0;
        LocalDate date = startDate;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(hours.size() == 7){
                double total = 0;
                for (Double hour : hours) {
                    total += hour;
                }
                if(total > 72){
                    LocalDate theatre = findShift(date.minusDays(7), doctor);
                    if(theatre != null){
                        if(freeWeeks(theatre, doctor)){
                            swaps = true;
                            break;
                        }
                    }
                    errorCounter++;
                }
                hours.remove(0);
            }
            if(doctor.getShiftType(date).equals(Shifts.DAYON) || doctor.getShiftType(date).equals(Shifts.NIGHT)){
                hours.add(12.5);
            }else if(doctor.getShiftType(date).equals(Shifts.THEATRE)) {
                hours.add(10.0);
            }else{
                hours.add(0.0);
            }
            date = date.plusDays(1);
        }
        return errorCounter;

    }



    private static LocalDate findShift(LocalDate date, JuniorDoctor doctor){
        for(int i=0; i< 7; i++){
            if(doctor.getShiftType(date).equals(Shifts.THEATRE)){
                return date;
            }
            date = date.plusDays(1);
        }
        return null;
    }

    private static boolean freeWeeks(LocalDate dateToSwap, JuniorDoctor doctor){
        ArrayList<Double> hours = new ArrayList<>();
        ArrayList<LocalDate> startDates = new ArrayList<>();

        LocalDate date = startDate;
        while (!date.isEqual(endDate.plusDays(1))) {
            if(hours.size() == 7){
                double total = 0;
                for (Double hour : hours) {
                    total += hour;
                }
                if((total + 10) < 72){
                    startDates.add(date.minusDays(7));
                    hours.clear();
                }else{
                    hours.remove(0);
                }
            }
            if(doctor.getShiftType(date).equals(Shifts.DAYON) || doctor.getShiftType(date).equals(Shifts.NIGHT)){
                hours.add(12.5);
            }else if(doctor.getShiftType(date).equals(Shifts.THEATRE)) {
                hours.add(10.0);
            }else{
                hours.add(0.0);
            }
            date = date.plusDays(1);
        }
        if(startDates.size() == 0){
            return false;
        }
        return swapShifts(doctor, dateToSwap, startDates);
    }

    private static boolean swapShifts(JuniorDoctor doctor, LocalDate dateToSwap, ArrayList<LocalDate> startDates){
        int counter = 0;
        boolean set = false;
        while(counter < 10 && !set) {
            int week = ThreadLocalRandom.current().nextInt(0, startDates.size());
            LocalDate date = startDates.get(week);
            if(notInPainWeek(doctor, date)) {
                for (int i = 0; i < 7; i++) {
                    if (doctor.getShiftType(date).equals(Shifts.DAYOFF) && !date.getDayOfWeek().equals(DayOfWeek.SATURDAY) &&
                            !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        doctor.setShifts(dateToSwap, Shifts.DAYOFF);
                        doctor.setShifts(date, Shifts.THEATRE);
                        set = true;
                        break;
                    }
                    date = date.plusDays(1);
                }
            }
            counter++;
        }
        return set;

    }

    private static boolean notInPainWeek(JuniorDoctor doctor, LocalDate date){
        if(!doctor.getPainWeek()){
            return true;
        }
        LocalDate start = doctor.getPainWeekStartDate();
        start = start.minusDays(2);
        for(int i=0; i<9; i++){
            if(start.equals(date)){
                return false;
            }
            start = start.plusDays(1);
        }
        return true;
    }

    private static int checkShiftCount(JuniorDoctor doctor){
        //change calculate to add together all doctors hours - either 1, 0.6 or 0.8
        int required =  (numberOfDays/doctors.size()) *2;
        int errorCounter = 0;
        int days = 0;
        int nights = 0;
        LocalDate date = startDate;
        while(!date.isEqual(endDate.plusDays(1))){
            if(doctor.getShiftType(date).equals(Shifts.DAYON)){
                days++;
            }
            else if(doctor.getShiftType(date).equals(Shifts.NIGHT)){
                nights++;
            }
            date = date.plusDays(1);
        }
        if((days + nights) < required){
            errorCounter++;
            //rulesBroken++;
        }
        return errorCounter;
    }

    private static int checkShiftHours(JuniorDoctor doctor){
        int errorCounter = 0;
        double hours = 0;
        LocalDate date = startDate;
        while(!date.isEqual(endDate.plusDays(1))){
            if(doctor.getShiftType(date).equals(Shifts.DAYON)){
                hours = hours + 12.5;
            }
            else if(doctor.getShiftType(date).equals(Shifts.NIGHT)){
                hours = hours + 12.5;
            }
            else if(doctor.getShiftType(date).equals(Shifts.THEATRE)){
                hours = hours + 10;
            }
            else if(doctor.getShiftType(date).equals(Shifts.AorSL)){
                hours = hours + 10;
            }
            date = date.plusDays(1);
        }
        if(hours < 570){
            errorCounter++;
            //rulesBroken++;
        }
        return errorCounter;
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

    public int getRulesBroken(){
        return rulesBroken;
    }

}
