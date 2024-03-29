package edu.uob;

import edu.uob.RotaBuilder.*;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;

final class RotaTests {
    private static final LocalDate startDate = LocalDate.of(2021, 8, 4);;
    private static final LocalDate endDate = LocalDate.of(2021, 11, 2);;
    private final static String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "Hugh", "Michael"};
    private final static double[] hours = {1,1,1,1,1,1,1,1};
    private static final Hashtable<LocalDate, ArrayList<Shifts>> fwp = new Hashtable<>();

    private void setStartAndEnd(ArrayList<JuniorDoctor> doctors){
        for(JuniorDoctor doctor : doctors){
            doctor.setStartDate(startDate);
            doctor.setEndDate(endDate);
        }
    }

    @Test
    void basicInitialisation() {
        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);
        setStartAndEnd(doctors);
        doctors.get(0).resetDoctor();
        assertEquals(doctors.get(0).getName(), "James");
        assertEquals(doctors.get(0).getLongDays(), 11);
        assertEquals(doctors.get(0).getNights(), 11);
        assertEquals(doctors.get(0).getTheatre(), 32);
        assertEquals(doctors.size(), 8);
        assertEquals(doctors.get(0).getEndDate(), endDate);
        assertEquals(doctors.get(0).getStartDate(), startDate);

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
            while (date.isBefore(endDate.plusDays(1))){
                if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                    assertNotEquals(doctor.getShiftType(date), Shifts.THEATRE);
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
            assertTrue((totalDays+totalNights) >= 22);
        }

        LocalDate date = startDate;
        int nt = 0;
        int dt = 0;
        while (date.isBefore(endDate.plusDays(1))) {

            int night = 0;
            int day =  0;
            for (JuniorDoctor doctor : doctors) {
                if(doctor.getShiftType(date).equals(Shifts.NIGHT)){
                    night++;
                    nt++;
                }
                if(doctor.getShiftType(date).equals(Shifts.DAYON)){
                    day++;
                    dt++;
                }
            }
            assertEquals(1, day);
            assertEquals(1, night);
            date = date.plusDays(1);
        }
        assertEquals(91, nt);
        assertEquals(91, dt);





    }


    @Test
    void painWeek(){
        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);
        setStartAndEnd(doctors);

        for(JuniorDoctor doctor : doctors){
            doctor.setPainWeek();
        }

        int numberOfDays = SortData.setNumberOfDays(startDate, endDate);

        BuildSchedule iteration;
        int rules;
        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors, fwp);
            rules = iteration.getRulesCount();
        } while (rules > 0);

        for(int i=0; i< doctors.size(); i++){
            for(int j=0; j< doctors.size(); j++){
                if(i != j){
                    assertNotEquals(doctors.get(i).getPainWeekStartDate(), doctors.get(j).getPainWeekStartDate());
                }
            }
        }
    }

    @Test
    void fixedWorkingPattern(){
        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);
        setStartAndEnd(doctors);
        int numberOfDays = SortData.setNumberOfDays(startDate, endDate);

        Hashtable<LocalDate, ArrayList<Shifts>> fixedWorkingPattern = new Hashtable<>();

        ArrayList<Shifts> s = new ArrayList<>();
        s.add(Shifts.NIGHT);
        fixedWorkingPattern.put(LocalDate.of(2021, 8, 20), s);
        fixedWorkingPattern.put(LocalDate.of(2021, 8, 21), s);
        fixedWorkingPattern.put(LocalDate.of(2021, 8, 22), s);

        fixedWorkingPattern.put(LocalDate.of(2021, 9, 17), s);
        fixedWorkingPattern.put(LocalDate.of(2021, 9, 18), s);
        fixedWorkingPattern.put(LocalDate.of(2021, 9, 19), s);

        fixedWorkingPattern.put(LocalDate.of(2021, 10, 15), s);
        fixedWorkingPattern.put(LocalDate.of(2021, 10, 16), s);
        fixedWorkingPattern.put(LocalDate.of(2021, 10, 17), s);

        BuildSchedule iteration;
        int rules;
        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors, fixedWorkingPattern);
            rules = iteration.getRulesCount();
        } while (rules > 0);

        for(JuniorDoctor doctor : doctors){
            assertNotEquals(doctor.getShiftType(LocalDate.of(2021,8,20)), Shifts.NIGHT);
            assertNotEquals(doctor.getShiftType(LocalDate.of(2021,8,21)), Shifts.NIGHT);
            assertNotEquals(doctor.getShiftType(LocalDate.of(2021,8,22)), Shifts.NIGHT);

            assertNotEquals(doctor.getShiftType(LocalDate.of(2021, 9, 17)), Shifts.NIGHT);
        }

    }




}
