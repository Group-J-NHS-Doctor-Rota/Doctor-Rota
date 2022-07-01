package edu.uob.prototype;

import java.time.LocalDate;
import java.util.ArrayList;

public class Schedule {
    private final static String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "Hugh", "Michael"};
    private static int numberOfDays;
    private static LocalDate startDate;
    private static LocalDate endDate;

    public static void main(String[] args) {

        startDate = LocalDate.of(2021, 8, 4);
        endDate = LocalDate.of(2021, 11, 2);
        setNumberOfDays();


        BuildSchedule iteration = new BuildSchedule(startDate, endDate, names, numberOfDays);

        int num = iteration.getRulesCount();
        System.out.println("Count = " + num);
        ArrayList<String> des = iteration.getDescription();

        for (String d : des){
            System.out.println(d);
        }

        ArrayList<JuniorDoctor> names = iteration.returnDoctors();

        printSchedule(names);
        checkNights(names);
        checkDays(names);


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
            for(JuniorDoctor doc : schedule){
                if(doc.getShifts(date).equals(Shifts.NIGHT)){
                    counter++;
                }
            }
            if (counter == 0){
                System.out.println("no night on day - " + date);
            }

            if(counter > 1){
                System.out.println("too many nights on - " + date + "amount = " + counter);
            }
            date = date.plusDays(1);
        }
    }

    private static void checkDays(ArrayList<JuniorDoctor> schedule){
        LocalDate date = startDate;
        LocalDate endPrint = endDate.plusDays(1);

        while (!date.isEqual(endPrint)) {
            int counter = 0;
            for(JuniorDoctor doc : schedule){
                if(doc.getShifts(date).equals(Shifts.DAYON)){
                    counter++;
                }
            }
            if (counter == 0){
                System.out.println("no days on day - " + date);
            }

            if(counter > 1){
                System.out.println("too many days on - " + date + "amount = " + counter);
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
            System.out.print(schedule.get(0).getShifts(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(1).getShifts(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(2).getShifts(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(3).getShifts(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(4).getShifts(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(5).getShifts(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(6).getShifts(date));
            System.out.print("  |  ");
            System.out.print(schedule.get(7).getShifts(date));
            System.out.print("  |  ");
            System.out.println();
            date = date.plusDays(1);
        }

    }
}
