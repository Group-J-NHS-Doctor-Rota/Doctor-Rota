package edu.uob.prototype;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

public class Schedule {
    private final static String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "Hugh", "Michael"};
    private static ArrayList<JuniorDoctor> doctors;
    private static int numberOfDays;
    private static LocalDate startDate;
    private static LocalDate endDate;

    public static void main(String[] args) {

        startDate = LocalDate.of(2021, 8, 4);
        endDate = LocalDate.of(2021, 11, 2);
        setNumberOfDays();
        doctors = new ArrayList<>();

        addDoctors();
        int rules;
        BuildSchedule iteration;

        System.out.println(doctors.size());

        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors);
            rules = iteration.getRulesCount();
            System.out.println("rules  == " + rules);
        }while(checkShift(iteration.returnDoctors()) || rules > 0);

        ArrayList<String> des = iteration.getDescription();

        for (String d : des){
            System.out.println(d);
        }

//        printSchedule(names);
//        checkNights(names);
//        checkDays(names);
//        printLeftovers(names);
//        addShifts(names);


    }

    private static boolean checkShift(ArrayList<JuniorDoctor> doctors){
        for(JuniorDoctor doctor : doctors){
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
                date = date.plusDays(1);
            }
            //System.out.println("hours == " + hours);
            if(hours < 570){
                return true;
            }
        }
        return false;
    }

    private static void addDoctors(){
        for (String doctorsName : names) {
            JuniorDoctor doctor = new JuniorDoctor();
            doctor.setName(doctorsName);
            doctors.add(doctor);
        }
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
                date = date.plusDays(1);
            }
            System.out.println("total hours  -> " + hours);
            System.out.println("weekly hours = " + hours/13);
        }
    }

    private static void setNumberOfDays(){
        LocalDate date = startDate;
        while (!date.isEqual(endDate)) {
            date = date.plusDays(1);
            numberOfDays++;
        }
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
            if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY) || date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
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
            }
            date = date.plusDays(1);
        }
    }


    private static void printSchedule(ArrayList<JuniorDoctor> schedule){

        System.out.println("DATE                  |  " + names[0] + "    |  " + names[1] + "    |  " + names[2] + "     |  " + names[3] + "     |  "
                + names[4] + "     |  " + names[5] + "    |  " + names[6] + "     |  " + names[7] + "    |  ");
        LocalDate date = startDate;
        LocalDate endPrint = endDate.plusDays(1);

        while (!date.isEqual(endPrint)) {
            System.out.print(date.getDayOfWeek() + "-> " + date);
            System.out.print("  |  ");
            System.out.print(schedule.get(0).getShiftType(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(1).getShiftType(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(2).getShiftType(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(3).getShiftType(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(4).getShiftType(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(5).getShiftType(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(6).getShiftType(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(7).getShiftType(date));
            System.out.print("  |  ");
            System.out.println();
            date = date.plusDays(1);
        }

    }
}
