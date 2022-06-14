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
    }
}
