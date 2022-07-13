package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

final class RotaTests {
    private static final LocalDate startDate = LocalDate.of(2021, 8, 4);;
    private static final LocalDate endDate = LocalDate.of(2021, 11, 2);;
    private final static String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "Hugh", "Michael"};

    @Test
    void basicInitialisation() {
        ArrayList<JuniorDoctor> doctors = Schedule.addDoctors();
        doctors.get(0).resetDoctor();
        assertEquals(doctors.get(0).getName(), "James");
        assertEquals(doctors.get(0).getLongDays(), 11);
        assertEquals(doctors.get(0).getNights(), 11);
        assertEquals(doctors.get(0).getTheatre(), 32);
        assertEquals(doctors.size(), 8);

        int numberOfDays = Schedule.setNumberOfDays(startDate, endDate);

        BuildSchedule iteration;
        int rulesBroken;
        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors);
            rulesBroken = iteration.getRulesCount();
        } while (rulesBroken > 0);


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
    void leaveAndNotOnCallRequests(){
        ArrayList<JuniorDoctor> doctors = Schedule.addDoctors();

        JuniorDoctor james = doctors.get(0);
        james.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 18), LeaveType.STUDY);
        james.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 19), LeaveType.STUDY);
        james.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 20), LeaveType.ANNUAL);

        assertTrue(james.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,18)));
        assertTrue(james.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,19)));
        assertTrue(james.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,20)));

        assertEquals(james.getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,10,18)), LeaveType.STUDY);
        assertEquals(james.getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,10,20)), LeaveType.ANNUAL);

        james.addNotOnCallRequest(LocalDate.of(2021,10, 1), NotOnCallRequestType.DAY);
        james.addNotOnCallRequest(LocalDate.of(2021,10, 2), NotOnCallRequestType.DAY);
        james.addNotOnCallRequest(LocalDate.of(2021,10, 3), NotOnCallRequestType.DAY);

        assertTrue(james.getNotOnCallRequest().containsKey(LocalDate.of(2021,10,1)));

        assertEquals(james.getNotOnCallRequestType(LocalDate.of(2021,10,1)), NotOnCallRequestType.DAY);

        JuniorDoctor sam = doctors.get(2);
        sam.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 18), LeaveType.ANNUAL);
        sam.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 19), LeaveType.ANNUAL);
        sam.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,10, 20), LeaveType.ANNUAL);

        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,18)));
        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,19)));
        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,20)));

        int numberOfDays = Schedule.setNumberOfDays(startDate, endDate);

        assertEquals(numberOfDays, 90);

        BuildSchedule iteration;
        int rules;
        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors);
            rules = iteration.getRulesCount();
        } while (rules > 0);

        //making sure that building the rota doesn't override any of the leave / not on call requests

        assertEquals(james.getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,10,18)), LeaveType.STUDY);
        assertEquals(james.getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,10,20)), LeaveType.ANNUAL);

        assertEquals(james.getNotOnCallRequestType(LocalDate.of(2021,10,1)), NotOnCallRequestType.DAY);

        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,18)));
        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,19)));
        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021,10,20)));


        ArrayList<JuniorDoctor> doctors2 = Schedule.addDoctors();

        for(int i=0; i< doctors2.size() -3; i++){
            JuniorDoctor doctor = doctors2.get(i);
            doctor.addAnnualOrStudyLeaveRequest(LocalDate.of(2021,9, 18), LeaveType.STUDY);
        }

        assertEquals(doctors2.get(0).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertEquals(doctors2.get(1).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertEquals(doctors2.get(2).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertEquals(doctors2.get(3).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertEquals(doctors2.get(4).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(5).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(6).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(7).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);



        BuildSchedule iteration2;
        int rules2;
        do {
            iteration2 = new BuildSchedule(startDate, endDate, numberOfDays, doctors2);
            rules2 = iteration2.getRulesCount();
        } while (rules2 > 0);
//
        assertEquals(doctors2.get(0).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertEquals(doctors2.get(1).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertEquals(doctors2.get(2).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertEquals(doctors2.get(3).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertEquals(doctors2.get(4).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(5).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(6).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(7).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021,9,18)), LeaveType.STUDY);

        ArrayList<JuniorDoctor> doctors3 = Schedule.addDoctors();

        doctors3.get(0).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 16), LeaveType.STUDY);
        doctors3.get(0).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 17), LeaveType.STUDY);



        doctors3.get(1).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 22), LeaveType.ANNUAL);
        doctors3.get(1).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 23), LeaveType.ANNUAL);
        doctors3.get(1).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 24), LeaveType.ANNUAL);



        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 27), LeaveType.ANNUAL);
        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 28), LeaveType.ANNUAL);
        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 29), LeaveType.ANNUAL);
        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 30), LeaveType.ANNUAL);
        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 1), LeaveType.ANNUAL);

        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 14), LeaveType.STUDY);

        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 27), LeaveType.ANNUAL);
        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 28), LeaveType.ANNUAL);
        doctors3.get(2).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 29), LeaveType.ANNUAL);

        doctors3.get(5).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 8, 16), LeaveType.ANNUAL);
        doctors3.get(5).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 8, 17), LeaveType.ANNUAL);
        doctors3.get(5).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 8, 18), LeaveType.ANNUAL);
        doctors3.get(5).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 8, 19), LeaveType.ANNUAL);

        doctors3.get(7).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 8, 16), LeaveType.ANNUAL);
        doctors3.get(7).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 8, 17), LeaveType.ANNUAL);
        doctors3.get(7).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 8, 19), LeaveType.ANNUAL);
        doctors3.get(7).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 8, 20), LeaveType.ANNUAL);
        doctors3.get(7).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 18), LeaveType.ANNUAL);
        doctors3.get(7).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 25), LeaveType.ANNUAL);
        doctors3.get(7).addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 26), LeaveType.ANNUAL);


        BuildSchedule iteration3;
        int rules3;
        do {
            iteration3 = new BuildSchedule(startDate, endDate, numberOfDays, doctors3);
            rules3 = iteration3.getRulesCount();
        } while (rules3 > 0);




    }


    @Test
    void testRuleBreaks() {

        //test that more than 7 days in a row == error

        ArrayList<JuniorDoctor> doctors1 = Schedule.addDoctors();

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

        ArrayList<JuniorDoctor> doctors = Schedule.addDoctors();
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




}
