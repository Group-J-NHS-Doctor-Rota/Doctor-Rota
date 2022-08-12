package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RulesBroken {

    private static final LocalDate startDate = LocalDate.of(2021, 8, 4);;
    private static final LocalDate endDate = LocalDate.of(2021, 11, 2);;
    private final static String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "Hugh", "Michael"};
    private final static double[] hours = {1,1,1,1,1,1,1,1};
    private static final Hashtable<LocalDate, ArrayList<Shifts>> fwp = new Hashtable<>();

    @Test
    void testRuleBreaks() {

        //test that more than 7 days in a row == error

        ArrayList<JuniorDoctor> doctors1 = SortData.addDoctors(names, hours);

        JuniorDoctor alex = doctors1.get(1);
        // assigning shifts - 7 in a row which is allowed
        alex.setShifts(LocalDate.of(2021,10,1), Shifts.THEATRE);
        alex.setShifts(LocalDate.of(2021,10,2), Shifts.THEATRE);
        alex.setShifts(LocalDate.of(2021,10,3), Shifts.THEATRE);
        alex.setShifts(LocalDate.of(2021,10,4), Shifts.THEATRE);
        alex.setShifts(LocalDate.of(2021,10,5), Shifts.THEATRE);
        alex.setShifts(LocalDate.of(2021,10,6), Shifts.THEATRE);
        alex.setShifts(LocalDate.of(2021,10,7), Shifts.THEATRE);

        BuildSchedule.setOffDays(startDate, endDate, doctors1);

        //check rules score == 0
        assertEquals(Rules.sevenConsecutiveShifts(alex, startDate, endDate), 0);
        //add additional shift (breaking rule) - doesn't even have to be next shift
        // as meant to have 48 hours off after 7 consecutive shifts
        alex.setShifts(LocalDate.of(2021,10,9), Shifts.THEATRE);
        //check that rule count == 1
        assertEquals(Rules.sevenConsecutiveShifts(alex, startDate, endDate), 1);

        //james.setShifts(LocalDate.of(2021,9,20), Shifts.DAYON);

        //test 4 days in a row rule

        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);
        JuniorDoctor james = doctors.get(0);

        james.setShifts(LocalDate.of(2021,9,20), Shifts.DAYON);
        james.setShifts(LocalDate.of(2021,9,21), Shifts.DAYON);
        james.setShifts(LocalDate.of(2021,9,22), Shifts.DAYON);
        james.setShifts(LocalDate.of(2021,9,23), Shifts.DAYON);


        BuildSchedule.setOffDays(startDate, endDate, doctors);

        assertEquals(Rules.fourLongDaysInARow(james, startDate, endDate), 0);

        james.setShifts(LocalDate.of(2021,9,24), Shifts.THEATRE);
        assertEquals(Rules.fourLongDaysInARow(james, startDate, endDate), 1);


        //less than 22 day + nights

        //three weekends in a row
        JuniorDoctor sam = doctors.get(2);

        //weekend 1
        sam.setShifts(LocalDate.of(2021,8,7), Shifts.DAYON);
        sam.setShifts(LocalDate.of(2021,8,8), Shifts.DAYON);

        //weekend 2
        sam.setShifts(LocalDate.of(2021,8,14), Shifts.DAYON);
        sam.setShifts(LocalDate.of(2021,8,15), Shifts.DAYON);

        //two weekends in a row does not cause issues
        assertEquals(sam.getShiftType(LocalDate.of(2021,8,14)), Shifts.DAYON);
        assertEquals(sam.getShiftType(LocalDate.of(2021,8,7)), Shifts.DAYON);
        assertEquals(sam.getShiftType(LocalDate.of(2021,8,20)), Shifts.DAYOFF);
        assertEquals(Rules.threeWeekendsInARow(sam, startDate, endDate), 0);

        sam.setShifts(LocalDate.of(2021,8,21), Shifts.DAYON);
        sam.setShifts(LocalDate.of(2021,8,22), Shifts.DAYON);

        assertEquals(sam.getShiftType(LocalDate.of(2021,8,21)), Shifts.DAYON);
        assertEquals(Rules.threeWeekendsInARow(sam, startDate, endDate), 1);


        //72 hour work week
        JuniorDoctor bob = doctors.get(3);

        bob.setShifts(LocalDate.of(2021,8,9), Shifts.DAYON);
        bob.setShifts(LocalDate.of(2021,8,10), Shifts.DAYON);
        bob.setShifts(LocalDate.of(2021,8,11), Shifts.DAYON);
        bob.setShifts(LocalDate.of(2021,8,12), Shifts.DAYON);
        bob.setShifts(LocalDate.of(2021,8,13), Shifts.DAYON);


        assertEquals(Rules.seventyTwoHourWorkWeek(bob, startDate, endDate), 0);

        bob.setShifts(LocalDate.of(2021,8,14), Shifts.DAYON);

        assertEquals(Rules.seventyTwoHourWorkWeek(bob, startDate, endDate), 2);



    }

    @Test
    void LowerHigher(){
        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);

        JuniorDoctor james = doctors.get(0);

        JuniorDoctor alex = doctors.get(1);
    }
}
