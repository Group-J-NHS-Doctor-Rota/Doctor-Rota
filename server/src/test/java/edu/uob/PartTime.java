package edu.uob;

import edu.uob.RotaBuilder.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

public class PartTime {
    private static final LocalDate startDate = LocalDate.of(2021, 8, 4);;
    private static final LocalDate endDate = LocalDate.of(2021, 11, 2);;
    private static final Hashtable<LocalDate, ArrayList<Shifts>> fwp = new Hashtable<>();

    @Test
    void fifteenPartTimeZeroPointSix(){
        String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "michael", "steve", "paul", "daniel", "sarah", "amy", "ella", "megan", "sheila"};
        double[] hours = {0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6, 0.6};

        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);

        doctors.get(0).resetDoctor();

        assertEquals(doctors.get(0).getName(), "James");
        assertEquals(doctors.get(0).getLongDays(), 6);
        assertEquals(doctors.get(0).getNights(), 6);
        assertEquals(doctors.get(0).getTheatre(), 19);
        assertEquals(doctors.get(0).getWeekends(), 2);
        assertEquals(doctors.size(), 15);

        int numberOfDays = SortData.setNumberOfDays(startDate, endDate);
        assertEquals(numberOfDays, 90);

        BuildSchedule iteration;
        int rules;
        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors, fwp);
            rules = iteration.getRulesCount();
        } while (rules > 0);

        assertEquals(0, iteration.getRulesCount());

        for (JuniorDoctor doctor : doctors) {
            assertEquals(0, Rules.fourLongDaysInARow(doctor, startDate, endDate));
            assertEquals(0, Rules.threeWeekendsInARow(doctor, startDate, endDate));
            assertEquals(0, Rules.sevenConsecutiveShifts(doctor, startDate, endDate));
            assertEquals(0, Rules.seventyTwoHourWorkWeek(doctor, startDate, endDate));
            LocalDate date = startDate;
            int nightCount = 0;
            int dayCount = 0;
            int totalNights = 0;
            int totalDays = 0;
            int weekendCount = 0;
            int theatreCount = 0;
            while (date.isBefore(endDate.plusDays(1))){
                if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
                    if(doctor.onShift(date)){
                        weekendCount++;
                    }
                }
                if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    assertNotEquals(doctor.getShiftType(date), Shifts.THEATRE);
                }
                if(doctor.getShiftType(date).equals(Shifts.THEATRE)){
                    theatreCount++;
                }
                if(doctor.getShiftType(date).equals(Shifts.NIGHT)){
                    nightCount++;
                    totalNights++;
                }else{
                    if(nightCount == 2){
                        assertEquals(doctor.getShiftType(date), Shifts.NAOFF);
                        assertEquals(doctor.getShiftType(date.plusDays(1)), Shifts.NAOFF);
                        nightCount = 0;
                    }
                    if(nightCount == 3){
                        assertEquals(doctor.getShiftType(date), Shifts.NAOFF);
                        assertEquals(doctor.getShiftType(date.plusDays(1)), Shifts.NAOFF);
                        assertEquals(doctor.getShiftType(date.plusDays(2)), Shifts.NAOFF);
                        nightCount = 0;
                    }
                }
                if(doctor.getShiftType(date).equals(Shifts.DAYON)) {
                    totalDays++;
                    if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
                        assertEquals(doctor.getShiftType(date.plusDays(1)), Shifts.DAYON);
                        assertEquals(doctor.getShiftType(date.plusDays(2)), Shifts.DAYON);
                        assertEquals(doctor.getShiftType(date.plusDays(3)), Shifts.DAOFF);
                        assertEquals(doctor.getShiftType(date.plusDays(4)), Shifts.DAOFF);
                    }else {
                        dayCount++;
                    }
                }else{
                    if(dayCount == 4){
                        assertFalse(doctor.onShift(date));
                        assertFalse(doctor.onShift(date.plusDays(1)));
                    }
                    dayCount = 0;
                }
                date = date.plusDays(1);
            }

            assertTrue(weekendCount <= 2);
            if((totalDays+totalNights) > 12){
                int count = (totalDays+totalNights) - 12;
                assertEquals((19 - count), theatreCount);
            }else{
                assertEquals(12, (totalDays+totalNights));
            }
        }
    }
    @Test
    void mixedPartTimeAndFullTime(){
        String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "michael", "steve", "paul"};
        double[] hours = {1,1,1,1,1,1,1,0.6,0.6};

        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);

        doctors.get(0).resetDoctor();
        doctors.get(8).resetDoctor();

        assertEquals(doctors.get(0).getName(), "James");
        assertEquals(doctors.get(0).getLongDays(), 11);
        assertEquals(doctors.get(0).getNights(), 11);
        assertEquals(doctors.get(0).getTheatre(), 32);
        assertEquals(doctors.get(0).getWeekends(), 3);

        assertEquals(doctors.get(8).getName(), "paul");
        assertEquals(doctors.get(8).getLongDays(), 6);
        assertEquals(doctors.get(8).getNights(), 6);
        assertEquals(doctors.get(8).getTheatre(), 19);
        assertEquals(doctors.get(8).getWeekends(), 2);

        assertEquals(doctors.size(), 9);

        int numberOfDays = SortData.setNumberOfDays(startDate, endDate);
        assertEquals(numberOfDays, 90);

        BuildSchedule iteration;
        int rules;
        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors, fwp);
            rules = iteration.getRulesCount();
        } while (rules > 0);

        for (int i=0; i<doctors.size(); i++) {
            JuniorDoctor doctor = doctors.get(i);
            assertEquals(0, Rules.fourLongDaysInARow(doctor, startDate, endDate));
            assertEquals(0, Rules.threeWeekendsInARow(doctor, startDate, endDate));
            assertEquals(0, Rules.sevenConsecutiveShifts(doctor, startDate, endDate));
            assertEquals(0, Rules.seventyTwoHourWorkWeek(doctor, startDate, endDate));
            LocalDate date = startDate;
            int nightCount = 0;
            int dayCount = 0;
            int totalNights = 0;
            int totalDays = 0;
            int weekendCount = 0;
            int theatreCount = 0;
            while (date.isBefore(endDate.plusDays(1))){
                if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY)){
                    if(doctor.onShift(date)){
                        weekendCount++;
                    }
                }
                if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    assertNotEquals(doctor.getShiftType(date), Shifts.THEATRE);
                }
                if(doctor.getShiftType(date).equals(Shifts.THEATRE)){
                    theatreCount++;
                }
                if(doctor.getShiftType(date).equals(Shifts.NIGHT)){
                    nightCount++;
                    totalNights++;
                }else{
                    if(nightCount == 2){
                        assertEquals(doctor.getShiftType(date), Shifts.NAOFF);
                        assertEquals(doctor.getShiftType(date.plusDays(1)), Shifts.NAOFF);
                        nightCount = 0;
                    }
                    if(nightCount == 3){
                        assertEquals(doctor.getShiftType(date), Shifts.NAOFF);
                        assertEquals(doctor.getShiftType(date.plusDays(1)), Shifts.NAOFF);
                        assertEquals(doctor.getShiftType(date.plusDays(2)), Shifts.NAOFF);
                        nightCount = 0;
                    }
                }
                if(doctor.getShiftType(date).equals(Shifts.DAYON)) {
                    totalDays++;
                    if(date.getDayOfWeek().equals(DayOfWeek.FRIDAY)){
                        assertEquals(doctor.getShiftType(date.plusDays(1)), Shifts.DAYON);
                        assertEquals(doctor.getShiftType(date.plusDays(2)), Shifts.DAYON);
                        assertEquals(doctor.getShiftType(date.plusDays(3)), Shifts.DAOFF);
                        assertEquals(doctor.getShiftType(date.plusDays(4)), Shifts.DAOFF);
                    }else {
                        dayCount++;
                    }
                }else{
                    if(dayCount == 4){
                        assertFalse(doctor.onShift(date));
                        assertFalse(doctor.onShift(date.plusDays(1)));
                    }
                    dayCount = 0;
                }
                date = date.plusDays(1);
            }

            if(i > 6) {
                assertTrue(weekendCount <= 2);
                if ((totalDays + totalNights) > 12) {
                    double hoursOver = ((totalDays + totalNights) - 12) * 12.5;
                    int count = (int) Math.round(hoursOver / 10);
                    //int count = (totalDays + totalNights) - 12;
                    assertEquals((19 - count), theatreCount);
                } else {
                    assertEquals(12, (totalDays + totalNights));
                }
            }else{
                //assertTrue(weekendCount <= 3);
                if ((totalDays + totalNights) > 22) {
                    double hoursOver = ((totalDays + totalNights) - 22) * 12.5;
                    int count = (int) Math.round(hoursOver / 10);
                    assertEquals((32 - count), theatreCount);
                } else {
                    assertEquals(22, (totalDays + totalNights));
                }
            }
        }
    }

    @Test
    void reducedWorkers(){

        String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "michael"};
        double[] hours = {1,1,1,1,1,1,1};
        int numberOfDays = SortData.setNumberOfDays(startDate, endDate);
        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);

        BuildSchedule iteration;
        int rules;
        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors, fwp);
            rules = iteration.getRulesCount();
        } while (rules > 0);

    }
}
