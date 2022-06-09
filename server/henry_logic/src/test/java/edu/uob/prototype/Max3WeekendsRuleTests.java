package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class Max3WeekendsRuleTests {

    @Test
    void testBasicShiftsAndRules() {
        Shift s1 = new Shift(ShiftTypes.NotWorking);
        Shift s2 = new Shift(ShiftTypes.NightOnCall);
        Shift s3 = new Shift(ShiftTypes.DayOnCall);
        // No weekends worked, rule met
        Rule r1 = new Max3WeekendsRule(s1, s1, s1, s1, s1, s1);
        assertFalse(r1.isBroken(), "Not even one weekend worked, rule shouldn't be broken");
        assertEquals(0, r1.getCost(), "0 cost expected");
        // Two weekends worked, rule met
        Rule r2 = new Max3WeekendsRule(s2, s2, s3, s3, s1, s1);
        assertFalse(r2.isBroken(), "Two weekends worked, rule shouldn't be broken");
        assertEquals(0, r2.getCost(), "0 cost expected");
        // Variation on two weekends worked, rule met
        Rule r3 = new Max3WeekendsRule(s2, s1, s1, s1, s1, s3);
        assertFalse(r3.isBroken(), "Two weekends worked, rule shouldn't be broken");
        assertEquals(0, r3.getCost(), "0 cost expected");
        // Three weekends worked, rule not met
        Rule r4 = new Max3WeekendsRule(s2, s2, s3, s3, s2, s2);
        assertTrue(r4.isBroken(), "Three weekends worked, rule should be broken");
        assertTrue(r4.getCost()>0, ">0 cost expected");
        // Variation on three weekends worked, rule not met
        Rule r5 = new Max3WeekendsRule(s3, s1, s1, s2, s2, s1);
        assertTrue(r5.isBroken(), "Three weekends worked, rule should be broken");
        assertTrue(r5.getCost()>0, ">0 cost expected");
    }
}
