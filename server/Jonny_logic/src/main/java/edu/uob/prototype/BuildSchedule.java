package edu.uob.prototype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class BuildSchedule {
    private static LocalDate startDate;
    private static LocalDate endDate;
    private static String[] doctorsNames;
    private static int numberOfDoctors;
    private static int numberOfDays;
    private static int rulesBroken;
    private static ArrayList<JuniorDoctor> doctors;
    private static ArrayList<String> description;


    public BuildSchedule(LocalDate start, LocalDate end, String[] names, int days){
        startDate = start;
        endDate = end;
        doctorsNames = names;
        numberOfDays = days;
        numberOfDoctors = doctorsNames.length;

        addShifts();
        System.out.println("shifts done");
        Rules rulesBrokenCount = new Rules(doctors, startDate, endDate);
        rulesBroken = rulesBrokenCount.getRulesBroken();
        description = rulesBrokenCount.getDescriptions();

    }

    public ArrayList<String> getDescription(){
        return description;
    }

    public ArrayList<JuniorDoctor> returnDoctors(){
        return doctors;
    }

    private static void addShifts(){

        int counter = 0;
        while(true) {
            counter++;
            doctors = new ArrayList<>();
            addDoctors();
            if(calculateNightShifts()) {
                addTheatreShifts();
                if (addLongDayShifts()) {
                    setOffDays();
                    break;
                }
            }
            System.out.println(counter);
        }

    }

    public int getRulesCount(){
        return rulesBroken;
    }

    private static void addWeekends(){

    }

    private static boolean calculateNightShifts(){
        int errorCounter = 0;
        LocalDate date = startDate;
        int difference = numberOfDays - (numberOfDoctors * 11);
        while (date.isBefore(endDate.minusDays(difference)) && errorCounter < 500) {
            int shiftPairing = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);
            JuniorDoctor doctor = doctors.get(shiftPairing);

            if(doctor.getNights() == 0){
                continue;
            }

            if(doctor.getNights() == 2 || doctor.getNights() == 4){
                if(addNightShifts(doctor, 2, date)) {
                    date = date.plusDays(2);
                }
                errorCounter++;
                continue;
            }

            if (doctor.getNights() == 3){
                if(addNightShifts(doctor, 3, date)) {
                    date = date.plusDays(3);
                }
                errorCounter++;
                continue;
            }

            int randomNum = ThreadLocalRandom.current().nextInt(0, numberOfDays + 1);
            if(randomNum % 2 == 0){
                if(addNightShifts(doctor, 2, date)) {
                    date = date.plusDays(2);
                }
            }else{
                if(addNightShifts(doctor, 3, date)) {
                    date = date.plusDays(3);
                }
            }
            errorCounter++;


        }
        return errorCounter != 500;
    }

    private static boolean addNightShifts(JuniorDoctor doctor, int numberOfShifts, LocalDate shift){

        if(!checkNightsFree(doctor, numberOfShifts, shift)){
            return false;
        }

        LocalDate date = shift;

        for (int i=0; i<numberOfShifts; i++){
            doctor.setShifts(date, Shifts.NIGHT);
            date = date.plusDays(1);
        }

        for (int i=0; i<numberOfShifts; i++){
            doctor.setShifts(date, Shifts.NAOFF);
            date = date.plusDays(1);
        }
        doctor.reduceNights(numberOfShifts);

        return  true;
    }

    private static boolean checkNightsFree(JuniorDoctor doctor, int numberOfShifts, LocalDate shift){
        int numberOfDays = numberOfShifts * 2;
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
                if (doctor.getShifts(date) == null) {
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
            int shiftPairing = ThreadLocalRandom.current().nextInt(0, numberOfDoctors);

            JuniorDoctor doctor = doctors.get(shiftPairing);

            if(doctor.getLongDays() == 0 || doctor.shiftTaken(date)){
                errorCounter++;
                continue;
            }

            doctor.setShifts(date, Shifts.DAYON);
            doctor.reduceLongDays();
            date = date.plusDays(1);

        }
        return  errorCounter != 500;
    }

    private static void addTheatreShifts(){
        for(JuniorDoctor doctor : doctors){
            int theatreCounter = doctor.getTheatre();
            int counter =0;
            while(counter<theatreCounter){
                int daySelection = ThreadLocalRandom.current().nextInt(0, numberOfDays +1);
                LocalDate date = startDate.plusDays(daySelection);
                if(!doctor.shiftTaken(date) && !date.getDayOfWeek().equals(DayOfWeek.SATURDAY)
                        && !date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
                    doctor.setShifts(date, Shifts.THEATRE);
                    counter++;
                }
            }
        }

    }


    private static void addDoctors(){
        for (String doctorsName : doctorsNames) {
            JuniorDoctor doctor = new JuniorDoctor();
            doctor.setName(doctorsName);
            doctors.add(doctor);
        }
    }
}
