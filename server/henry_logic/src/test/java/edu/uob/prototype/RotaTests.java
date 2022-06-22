package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;

final class RotaTests {

    @Test
    void testInitialisation() {
        Rota rota1 = new Rota(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-31"));
        Employee emp1 = new Employee("123456");
        Employee emp2 = new Employee("123457");
        rota1.addEmployee(emp1);
        rota1.addEmployee(emp2);
        // Check ids returned correctly
        ArrayList<String> ids = rota1.getEmployeeIds();
        assertTrue(ids.contains("123456"));
        assertTrue(ids.contains("123457"));
        RotaTools.createAllShiftsNotWorking(rota1);
        Shift s1 = rota1.getShift("123456", LocalDate.parse("2020-01-01"));
        assertEquals(ShiftTypes.NotWorking, s1.getType(), "Should be not working");
        Shift s2 = rota1.getShift("123456", LocalDate.parse("2020-01-31"));
        assertEquals(ShiftTypes.NotWorking, s2.getType(), "Should be not working");
        // Employee is not working outside of rota dates
        Shift s3 = rota1.getShift("123456", LocalDate.parse("2030-01-31"));
        assertEquals(ShiftTypes.NotWorking, s3.getType(), "Should be not working");
        // Check rules
        RotaTools.createRules(rota1);
        assertEquals(0, rota1.getCost(), "Shouldn't be any cost with all not working");
        // Number of days test
        assertEquals(31, rota1.getNumberOfDays(), "Should be 31 days");
    }

    @Test
    void testRuleBreaks() {
        Rota rota1 = new Rota(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-31"));
        Employee emp1 = new Employee("123456");
        Employee emp2 = new Employee("123457");
        rota1.addEmployee(emp1);
        rota1.addEmployee(emp2);
        RotaTools.createAllShiftsNotWorking(rota1);
        RotaTools.createRules(rota1);
        assertEquals(0, rota1.getCost(), "Shouldn't be any cost with all not working");
        // Night shift then normal shift is not allowed
        rota1.getShift("123456", LocalDate.parse("2020-01-01")).updateType(ShiftTypes.NightOnCall);
        rota1.getShift("123456", LocalDate.parse("2020-01-02")).updateType(ShiftTypes.NormalShift);
        rota1.refreshRules();
        assertTrue(rota1.getCost()>0, "Rule should be broken");
        ShiftTools.swapShifts(rota1.getShift("123456", LocalDate.parse("2020-01-02")),
                rota1.getShift("123456", LocalDate.parse("2020-01-04")));
        rota1.refreshRules();
        assertEquals(0, rota1.getCost(), "Rule should now be fixed");
    }

    @Test
    void testShiftSwaps() {
        Rota rota1 = new Rota(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-28"));
        Employee emp1 = new Employee("0001"); rota1.addEmployee(emp1);
        Employee emp2 = new Employee("0002"); rota1.addEmployee(emp2);
        Employee emp3 = new Employee("0003"); rota1.addEmployee(emp3);
        Employee emp4 = new Employee("0004"); rota1.addEmployee(emp4);
        Employee emp5 = new Employee("0005"); rota1.addEmployee(emp5);
        Employee emp6 = new Employee("0006"); rota1.addEmployee(emp6);
        Employee emp7 = new Employee("0007"); rota1.addEmployee(emp7);
        Employee emp8 = new Employee("0008"); rota1.addEmployee(emp8);

        RotaTools.createAllShifts(rota1);
        System.out.println(rota1);
        RotaTools.createRules(rota1);

        Shift s1 = rota1.getShift("0006", LocalDate.parse("2020-01-02"));
        Shift s2 = rota1.getShift("0007", LocalDate.parse("2020-01-02"));
        Shift s3 = rota1.getShift("0006", LocalDate.parse("2020-01-11"));
        assertFalse(ShiftTools.swapShifts(s1,s2));
        assertTrue(ShiftTools.swapShifts(s1,s3));

        System.out.println(rota1);

    }
}
