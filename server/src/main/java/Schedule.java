import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Schedule{

    private final static int numOfJuniorDoctors = 8;
    private final static String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "Hugh", "Michael"};
    private static ArrayList<JuniorDoctor> totalShifts;
    //private static ArrayList<ArrayList<JuniorDoctor>> collectionOfShifts;
    private static int numberOfDays;
    private static LocalDate startDate;
    private static LocalDate endDate;

    public static void main(String[] args) throws InterruptedException {

        startDate = LocalDate.of(2021, 8, 4);
        endDate = LocalDate.of(2021, 11, 2);
        setNumberOfDays();
        //collectionOfShifts = new ArrayList<>();


        for(int i=0; i<10; i++) {
            totalShifts = new ArrayList<>();

            Multithreading schedule = new Multithreading(numOfJuniorDoctors, numberOfDays, startDate, endDate, i);
            Thread thread = new Thread(schedule);
            thread.start();
            thread.join();
            totalShifts = schedule.schedule();
            Rules test = new Rules();
            boolean complete = test.Rules(totalShifts, startDate, numberOfDays);
            printSchedule(i, complete);
            //collectionOfShifts.add(totalShifts);
        }

    }


    private static void setNumberOfDays(){
        LocalDate date = startDate;
        while (!date.isEqual(endDate)) {
            date = date.plusDays(1);
            numberOfDays++;
        }
        numberOfDays++;

    }

    private static void printSchedule(int shiftCounter, boolean complete){
        System.out.println();
        System.out.println();
        System.out.println("shift = " + shiftCounter);
        System.out.println();
        System.out.println();
        System.out.println("no days not covered: " + complete);

        System.out.println("DATE        |  " + names[0] + "    |  " + names[1] + "    |  " + names[2] + "     |  " + names[3] + "     |  "
                + names[4] + "     |  " + names[5] + "    |  " + names[6] + "     |  " + names[7] + "    |  ");
        LocalDate date = startDate;
        LocalDate endPrint = endDate.plusDays(1);

        while (!date.isEqual(endPrint)) {
            System.out.print(date);
            System.out.print("  |  ");
            System.out.print(totalShifts.get(0).shifts.get(date));
            System.out.print("  |  ");
            System.out.print(totalShifts.get(1).shifts.get(date));
            System.out.print("  |  ");
            System.out.print(totalShifts.get(2).shifts.get(date));
            System.out.print("  |  ");
            System.out.print(totalShifts.get(3).shifts.get(date));
            System.out.print("  |  ");
            System.out.print(totalShifts.get(4).shifts.get(date));
            System.out.print("  |  ");
            System.out.print(totalShifts.get(5).shifts.get(date));
            System.out.print("  |  ");
            System.out.print(totalShifts.get(6).shifts.get(date));
            System.out.print("  |  ");
            System.out.print(totalShifts.get(7).shifts.get(date));
            System.out.print("  |  ");
            System.out.println();
            date = date.plusDays(1);
        }
    }
}
