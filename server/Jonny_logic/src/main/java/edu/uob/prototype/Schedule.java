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

        //testing pain week addition for first doctor
        for(JuniorDoctor doc : doctors){
            doc.setPainWeek();
        }

        JuniorDoctor a = doctors.get(0);
        a.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 18), LeaveType.STUDY);
        a.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 19), LeaveType.STUDY);
        a.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 20), LeaveType.STUDY);

        a.addNotOnCallRequest(LocalDate.of(2021,10, 1), NotOnCallRequestType.DAY);
        a.addNotOnCallRequest(LocalDate.of(2021,10, 2), NotOnCallRequestType.DAY);
        a.addNotOnCallRequest(LocalDate.of(2021,10, 3), NotOnCallRequestType.DAY);

        JuniorDoctor b = doctors.get(2);
        b.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 18), LeaveType.ANNUAL);
        b.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 19), LeaveType.ANNUAL);
        b.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 20), LeaveType.ANNUAL);




        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors);
            rules = iteration.getRulesCount();
        }while(rules > 0 || checkShiftCount(iteration.returnDoctors()) || checkShiftHours(iteration.returnDoctors()));

        doctors = iteration.returnDoctors();

        ArrayList<String> des = iteration.getDescription();

        for (String d : des){
            System.out.println(d);
        }

        printSchedule(doctors);
        checkNights(doctors);
        checkDays(doctors);
        printLeftovers(doctors);
        addShifts(doctors);


    }

    private static boolean checkShiftCount(ArrayList<JuniorDoctor> doctors){
        for(JuniorDoctor doctor : doctors){
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
            if(days+nights > 22){
                return true;
            }
        }
        return false;
    }

    private static boolean checkShiftHours(ArrayList<JuniorDoctor> doctors){
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
                else if(doctor.getShiftType(date).equals(Shifts.AorSL)){
                    hours = hours + 10;
                }
                date = date.plusDays(1);
            }
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
                else if(doctor.getShiftType(date).equals(Shifts.AorSL)){
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
