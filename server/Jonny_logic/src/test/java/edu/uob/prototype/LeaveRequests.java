package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Hashtable;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class LeaveRequests {

    private static final LocalDate startDate = LocalDate.of(2021, 8, 4);;
    private static final LocalDate endDate = LocalDate.of(2021, 11, 2);;
    private final static String [] names = {"James", "Alex", "Sam", "Bob", "Ryan", "Matt", "Hugh", "Michael"};
    private final static double[] hours = {1,1,1,1,1,1,1,1};
    private static final Hashtable<LocalDate, ArrayList<Shifts>> fwp = new Hashtable<>();

    @Test
    void leaveAndNotOnCallRequests() {
        ArrayList<JuniorDoctor> doctors = SortData.addDoctors(names, hours);

        JuniorDoctor james = doctors.get(0);
        james.addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 18), LeaveType.STUDY);
        james.addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 19), LeaveType.STUDY);
        james.addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 20), LeaveType.ANNUAL);

        assertTrue(james.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 18)));
        assertTrue(james.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 19)));
        assertTrue(james.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 20)));

        assertEquals(james.getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 10, 18)), LeaveType.STUDY);
        assertEquals(james.getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 10, 20)), LeaveType.ANNUAL);

        james.addNotOnCallRequest(LocalDate.of(2021, 10, 1), NotOnCallRequestType.DAY);
        james.addNotOnCallRequest(LocalDate.of(2021, 10, 2), NotOnCallRequestType.DAY);
        james.addNotOnCallRequest(LocalDate.of(2021, 10, 3), NotOnCallRequestType.DAY);

        assertTrue(james.getNotOnCallRequest().containsKey(LocalDate.of(2021, 10, 1)));

        assertEquals(james.getNotOnCallRequestType(LocalDate.of(2021, 10, 1)), NotOnCallRequestType.DAY);

        JuniorDoctor sam = doctors.get(2);
        sam.addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 18), LeaveType.ANNUAL);
        sam.addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 19), LeaveType.ANNUAL);
        sam.addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 10, 20), LeaveType.ANNUAL);

        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 18)));
        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 19)));
        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 20)));

        int numberOfDays = SortData.setNumberOfDays(startDate, endDate);

        assertEquals(numberOfDays, 90);

        BuildSchedule iteration;
        int rules;
        do {
            iteration = new BuildSchedule(startDate, endDate, numberOfDays, doctors, fwp);
            rules = iteration.getRulesCount();
        } while (rules > 0);

        //making sure that building the rota doesn't override any of the leave / not on call requests

        assertEquals(james.getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 10, 18)), LeaveType.STUDY);
        assertEquals(james.getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 10, 20)), LeaveType.ANNUAL);

        assertEquals(james.getNotOnCallRequestType(LocalDate.of(2021, 10, 1)), NotOnCallRequestType.DAY);

        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 18)));
        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 19)));
        assertTrue(sam.getAnnualOrStudyLeaveRequest().containsKey(LocalDate.of(2021, 10, 20)));


        ArrayList<JuniorDoctor> doctors2 = SortData.addDoctors(names, hours);

        for (int i = 0; i < doctors2.size() - 3; i++) {
            JuniorDoctor doctor = doctors2.get(i);
            doctor.addAnnualOrStudyLeaveRequest(LocalDate.of(2021, 9, 18), LeaveType.STUDY);
        }

        assertEquals(doctors2.get(0).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertEquals(doctors2.get(1).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertEquals(doctors2.get(2).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertEquals(doctors2.get(3).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertEquals(doctors2.get(4).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(5).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(6).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(7).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);


        BuildSchedule iteration2;
        int rules2;
        do {
            iteration2 = new BuildSchedule(startDate, endDate, numberOfDays, doctors2, fwp);
            rules2 = iteration2.getRulesCount();
        } while (rules2 > 0);

        assertEquals(doctors2.get(0).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertEquals(doctors2.get(1).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertEquals(doctors2.get(2).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertEquals(doctors2.get(3).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertEquals(doctors2.get(4).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(5).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(6).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);
        assertNotEquals(doctors2.get(7).getAnnualOrStudyLeaveRequestType(LocalDate.of(2021, 9, 18)), LeaveType.STUDY);

        ArrayList<JuniorDoctor> doctors3 = SortData.addDoctors(names, hours);

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
            iteration3 = new BuildSchedule(startDate, endDate, numberOfDays, doctors3, fwp);
            rules3 = iteration3.getRulesCount();
        } while (rules3 > 0);

        assertEquals(0, iteration3.getRulesCount());


    }
}
