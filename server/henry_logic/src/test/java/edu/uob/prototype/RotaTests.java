package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

final class RotaTests {

    @Test
    void testInitialisation() {
        Rota rota1 = new Rota(LocalDate.parse("2020-01-01"), LocalDate.parse("2020-01-31"));
        Employee emp1 = new Employee("123456");
        Employee emp2 = new Employee("123457");
        rota1.addEmployee(emp1);
        rota1.addEmployee(emp2);
        RotaTools.createAllShiftsNotWorking(rota1);
        Shift s1 = rota1.getShift("123456", LocalDate.parse("2020-01-01"));
        assertEquals(ShiftTypes.NotWorking, s1.getType(), "Should be not working");
        Shift s2 = rota1.getShift("123456", LocalDate.parse("2020-01-31"));
        assertEquals(ShiftTypes.NotWorking, s2.getType(), "Should be not working");
        // Employee is not working outside of rota dates
        Shift s3 = rota1.getShift("123456", LocalDate.parse("2030-01-31"));
        assertEquals(ShiftTypes.NotWorking, s3.getType(), "Should be not working");
    }
}
