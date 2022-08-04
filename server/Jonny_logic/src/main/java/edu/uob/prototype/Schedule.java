package edu.uob.prototype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;

public class Schedule {
    private final static String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "Will", "Adam"};
    private final static double[] hours = {1,1,1,1,1,1,1,1};
    private static LocalDate startDate;
    private static LocalDate endDate;

    public static void main(String[] args) {

        startDate = LocalDate.of(2021, 8, 4);
        endDate = LocalDate.of(2021, 11, 2);
        int numberOfDays = setNumberOfDays(startDate, endDate);

        long startTime = System.nanoTime();

        ArrayList<JuniorDoctor> doctors = addDoctors(names, hours);

        Hashtable<LocalDate, ArrayList<Shifts>> fixedWorkingPattern = new Hashtable<>();

        ArrayList<String> rulesBrokenDescription = new ArrayList<>();



        int rulesBroken;
        int counter = 0;
        do {
            BuildSchedule iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors, fixedWorkingPattern);
            rulesBroken = iteration.getRulesCount();
            rulesBrokenDescription = iteration.getDescriptions();
            counter++;
        } while (rulesBroken > 0);


//        long endTime = System.nanoTime();
//        long totalTime = endTime - startTime;
//        double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
//        System.out.println(elapsedTimeInSecond);


        printSchedule(doctors, startDate, endDate);
        System.out.println(rulesBrokenDescription);
        checkNights(doctors);
        checkDays(doctors);
        printLeftovers(doctors);
        addShifts(doctors);


    }


    public static ArrayList<JuniorDoctor> addDoctors(String[] names, double[] hours){
        ArrayList<JuniorDoctor> doctors = new ArrayList<>();
        for (int i=0; i<names.length; i++) {
            JuniorDoctor doctor = new JuniorDoctor(hours[i]);
            doctor.setName(names[i]);
            doctors.add(doctor);
            //doctor.setPainWeek();
        }
        return doctors;
    }

    private static void addShifts(ArrayList<JuniorDoctor> doctors){
        int days;
        int nights;
        int theatre;
        for(JuniorDoctor doctor : doctors){
            days = 0;
            nights = 0;
            theatre = 0;
            System.out.println(doctor.getName());
            LocalDate date = startDate;
            while(!date.isEqual(endDate.plusDays(1))){
                if(doctor.getShiftType(date).equals(Shifts.DAYON)){
                    days++;
                }
                else if(doctor.getShiftType(date).equals(Shifts.NIGHT)){
                    nights++;
                }
                else if(doctor.getShiftType(date).equals(Shifts.THEATRE)){
                    theatre++;
                }
                date = date.plusDays(1);
            }
            System.out.println("days = " + days);
            System.out.println("nights = " + nights);
            System.out.println("theatre = " + theatre);
            if(doctor.getPainWeekStartDate() != null){
                System.out.println("pain week start = " + doctor.getPainWeekStartDate());
            }else{
                System.out.println("no pain week");
            }
        }
    }

    private static void printLeftovers(ArrayList<JuniorDoctor> doctors){
        double hours;
        for (JuniorDoctor doctor : doctors){
            hours = 0;
            System.out.println(doctor.getName());
            System.out.println("weekends -> " + doctor.getWeekends());
            System.out.println("Nights -> " + doctor.getNights());
            System.out.println("Days - > " + doctor.getLongDays());
            System.out.println("Theatre -> " + doctor.getTheatre());
            System.out.println("Total on call ->" + doctor.getTotalOnCall());
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
                else if(doctor.getShiftType(date).equals(Shifts.ANNUAL) || doctor.getShiftType(date).equals(Shifts.STUDY)){
                    hours = hours + 10;
                }
                date = date.plusDays(1);
            }
            System.out.println("total hours  -> " + hours);
            System.out.println("weekly hours = " + hours/13);
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

    private static void checkNights(ArrayList<JuniorDoctor> schedule){
        LocalDate date = startDate;
        LocalDate endPrint = endDate.plusDays(1);

        while (!date.isEqual(endPrint)) {
            int counter = 0;
                for (JuniorDoctor doc : schedule) {
                    if (doc.getShiftType(date).equals(Shifts.NIGHT)) {
                        counter++;
                    }
                }
                if (counter == 0) {
                    System.out.println("no night on day - " + date + "->" + date.getDayOfWeek());
                }

                if (counter > 1) {
                    System.out.println("too many nights on - " + date + "amount = " + counter);
                }
            //}
            date = date.plusDays(1);
        }
    }

    private static void checkDays(ArrayList<JuniorDoctor> schedule){
        LocalDate date = startDate;
        LocalDate endPrint = endDate.plusDays(1);

        while (!date.isEqual(endPrint)) {
            //if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY) || date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                int counter = 0;
                for (JuniorDoctor doc : schedule) {
                    if (doc.getShiftType(date).equals(Shifts.DAYON)) {
                        counter++;
                    }
                }
                if (counter == 0) {
                    System.out.println("no days on day - " + date + "-> " + date.getDayOfWeek());
                }

                if (counter > 1) {
                    System.out.println("too many days on - " + date + "amount = " + counter);
                }
            //}
            date = date.plusDays(1);
        }
    }


    public static void printSchedule(ArrayList<JuniorDoctor> schedule, LocalDate startDate, LocalDate endDate){

        System.out.print("DATE                  |  ");
        for(int i=0; i< names.length; i++){
            System.out.print(names[i]);
            System.out.print("  |  ");
        }
        System.out.println();

        LocalDate date = startDate;
        LocalDate endPrint = endDate.plusDays(1);

        while (date.isBefore(endDate.plusDays(1))) {
            System.out.print(date.getDayOfWeek() + "-> " + date);
            System.out.print("  |  ");
            for(int i=0; i< schedule.size(); i++){
                System.out.print(schedule.get(i).getShiftType(date));
                System.out.print("  |  ");
            }
            System.out.println();
            date = date.plusDays(1);
        }

    }
}
