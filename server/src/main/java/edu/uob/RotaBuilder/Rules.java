package edu.uob.RotaBuilder;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.ThreadLocalRandom;

public class Rules {
    private static LocalDate startDate;
    private static LocalDate endDate;
    private static int rulesBroken;
    private static int numberOfDays;
    private static boolean swaps;
//    private static ArrayList<String> description;


    public Rules(ArrayList<JuniorDoctor> doctorsList, LocalDate start, LocalDate end){
        startDate = start;
        endDate = end;
        numberOfDays = setNumberOfDays(startDate, endDate);

        rulesBroken(doctorsList, start, end, 0);

    }

    private static void rulesBroken(ArrayList<JuniorDoctor> doctors, LocalDate start, LocalDate end, int count){
//        description = new ArrayList<>();
        if(count == 10){
            rulesBroken = 100;
            return;
        }
        rulesBroken = 0;
        for(JuniorDoctor doctor : doctors){
            rulesBroken += fourLongDaysInARow(doctor, start, end);
            rulesBroken += sevenConsecutiveShifts(doctor, start, end);
            rulesBroken += threeWeekendsInARow(doctor, start, end);
            rulesBroken += fortyEightHourWeek(doctor);
        }


        if(rulesBroken == 0){
            //recursion used to swap shifts to try and fit in 72 hour rule
            for(JuniorDoctor doctor : doctors) {
                swaps = false;
                int counter = seventyTwoHourWorkWeek(doctor, start, end);
                if(swaps && counter == 0){
                    rulesBroken(doctors, start, end, count+1);
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
        rulesBroken += requiredNormalShiftsPerDay(doctors);
        rulesBroken += lowerOrHigher(doctors);
    }

    public static int painWeek(JuniorDoctor doctor){
        //checks pain week conforms to required pattern
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
//        if(errors > 0){
//            description.add("Error with pain week -> " + doctor.getName());
//        }
        return errors;
    }

    public static int fourLongDaysInARow(JuniorDoctor doctor, LocalDate startDate, LocalDate endDate){
        //checks that junior doctor doesn't have more than 4 long day shifts in a row
        //if they have 4 in a row -  checks that next 48 hours they have off
        int errorCounter = 0;
        LocalDate date = startDate;
        int counter = 0;
        while (date.isBefore(endDate.plusDays(1))) {
            if(counter > 4){
                errorCounter++;
//                description.add("Four long days in a row from - " + date.minusDays(5) + "to - " + date + " -> " + doctor.getName());
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
        //not working more than 7 consecutive shifts
        //if working 7 make sure next 48 hours they have off
        int errorCounter = 0;
        LocalDate date = startDate;
        int counter = 0;
        while (date.isBefore(endDate.plusDays(1))) {
            if(counter == 7){
                errorCounter += check48Hours(date, doctor);
//                if (check48Hours(date, doctor) > 0){
//                    description.add("more than seven consecutive shifts starting at - " + date.minusDays(7) + " -> " + doctor.getName());
//                }
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
        //checks next 48 hours they have off
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
        //not working three weekends in a row
        int errorCounter = 0;
        LocalDate date = startDate;
        int totalWeekends = 0;
        int weekendOnCounter = 0;
        while (date.isBefore(endDate.plusDays(1))) {
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
//        if(errorCounter>0){
//            description.add("three weekends in a row -> " + doctor.getName());
//        }
        return errorCounter;
    }


    public static int seventyTwoHourWorkWeek(JuniorDoctor doctor, LocalDate startDate, LocalDate endDate){
        ArrayList<Double> hours = new ArrayList<>();

        int errorCounter = 0;
        LocalDate date = startDate;
        while (date.isBefore(endDate.plusDays(1))) {
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
//        if(errorCounter > 0){
//            description.add("72 hour work week -> " +  doctor.getName());
//        }
        return errorCounter;

    }



    private static LocalDate findShift(LocalDate date, JuniorDoctor doctor){
        //used to find free shift that can be swapped
        for(int i=0; i< 7; i++){
            if(doctor.getShiftType(date).equals(Shifts.THEATRE)){
                return date;
            }
            date = date.plusDays(1);
        }
        return null;
    }

    private static boolean freeWeeks(LocalDate dateToSwap, JuniorDoctor doctor){
        //finds weeks that they are working less than 72 hours even if a theatre shift is added to it (10 hours)
        ArrayList<Double> hours = new ArrayList<>();
        ArrayList<LocalDate> startDates = new ArrayList<>();

        LocalDate date = startDate;
        while (date.isBefore(endDate.plusDays(1))) {
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
        //Swap shifts
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
        //checks that free shift found for swap isn't in the pain week
        //can't be on call during pain week
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


    public static int setNumberOfDays(LocalDate startDate, LocalDate endDate){
        LocalDate date = startDate;
        int counter = 0;
        while (date.isBefore(endDate.plusDays(1))) {
            date = date.plusDays(1);
            counter++;
        }
        return counter;
    }

    public static int fortyEightHourWeek(JuniorDoctor doctor){
        //don't have a higher average than 48hours working week across rota
        double hours = 0;
        LocalDate date = startDate;
        while(date.isBefore(endDate.plusDays(1))){
            if(doctor.getShiftType(date).equals(Shifts.DAYON)){
                hours = hours + 12.5;
            }
            else if(doctor.getShiftType(date).equals(Shifts.NIGHT)){
                hours = hours + 12.5;
            }
            else if(doctor.getShiftType(date).equals(Shifts.THEATRE)){
                hours = hours + 10;
            }
            else if(doctor.getShiftType(date).equals(Shifts.ANNUAL) || doctor.getShiftType(date).equals(Shifts.STUDY)){
                hours = hours + 10;
            }
            date = date.plusDays(1);
        }
        int weeks = numberOfDays/7;
        if((hours / weeks) > 48){
//            description.add("More than 48 hour average work week");
            return 1;
        }
        return 0;

    }

    public static int requiredNormalShiftsPerDay(ArrayList<JuniorDoctor> doctors){
        //at least 2 theatre shifts on each day
        LocalDate date = startDate;
        int errorCounter = 0;
        while(date.isBefore(endDate.plusDays(1))){
            if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                    || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                date = date.plusDays(1);
                continue;
            }
            int counter = 0;
            for(JuniorDoctor doctor : doctors){
                if(doctor.getShiftType(date).equals(Shifts.THEATRE)){
                    counter++;
                }
            }
            if(counter < 2){
//                description.add("not enough normal shifts on day -> " +date);
                errorCounter++;
            }
            date = date.plusDays(1);
        }
        return errorCounter;
    }

    public static int lowerOrHigher(ArrayList<JuniorDoctor> doctors){
        //makes sure that if someone is doing extra on call shift no one is doing under
        //tryies to make it as even as possible across all doctors
        boolean less = false;
        boolean more = false;

        for(JuniorDoctor doctor : doctors){
            LocalDate date = startDate;
            int required = doctor.getSetLongDays() + doctor.getSetNights();
            int totalDaysAndNights = 0;
            while(date.isBefore(endDate.plusDays(1))){
                if(doctor.getShiftType(date).equals(Shifts.NIGHT) || doctor.getShiftType(date).equals(Shifts.DAYON)){
                    totalDaysAndNights++;
                }
                date = date.plusDays(1);
            }
            if(required > totalDaysAndNights){
                less = true;
            }else if(required < totalDaysAndNights){
                more = true;
            }
        }
        if(less && more){
            return 1;
        }
        return 0;
    }

    public int getRulesBroken(){
        return rulesBroken;
    }

//    public ArrayList<String> getRulesBrokenDescriptions(){
//        return description;
//    }

}
