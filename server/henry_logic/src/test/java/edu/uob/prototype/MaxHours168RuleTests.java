package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

final class MaxHours168RuleTests {

    @Test
    void testBasicShiftsAndRules() {
        Shift s1 = new Shift(ShiftTypes.NotWorking);
        Shift s2 = new Shift(ShiftTypes.NormalShift);
        Shift s3 = new Shift(ShiftTypes.NightOnCall);
        Shift s4 = new Shift(ShiftTypes.DayOnCall);
        // Non-working week, rule should be met
        Rule r1 = new MaxHours168Rule(s1, s1, s1, s1, s1, s1, s1);
        assertFalse(r1.isBroken(), "0 hour week should be less than 72");
        assertEquals(0, r1.getCost(), "0 cost expected");
        // Full normal shifts, rule should be met
        Rule r2 = new MaxHours168Rule(s2, s2, s2, s2, s2, s2, s2);
        assertFalse(r2.isBroken(), "70 hour week should be less than 72");
        assertEquals(0, r2.getCost(), "0 cost expected");
        // Full night shifts, rule should not be met
        Rule r3 = new MaxHours168Rule(s3, s3, s3, s3, s3, s3, s3);
        assertTrue(r3.isBroken(), "87.5 hour week should be greater than 72");
        assertEquals(200, r3.getCost(), "200 cost expected");
        // Full day on call shifts, rule should not be met
        Rule r4 = new MaxHours168Rule(s4, s4, s4, s4, s4, s4, s4);
        assertTrue(r4.isBroken(), "87.5 hour week should be greater than 72");
        assertEquals(200, r4.getCost(), "200 cost expected");
        // Mix of shifts, rule should be met
        Rule r5 = new MaxHours168Rule(s1, s2, s2, s2, s3, s2, s2);
        assertFalse(r5.isBroken(), "62.5 hour week should be less than 72");
        assertEquals(0, r5.getCost(), "0 cost expected");
        // Mix of shifts, rule should not be met
        Rule r6 = new MaxHours168Rule(s3, s4, s1, s4, s3, s2, s3);
        assertTrue(r6.isBroken(), "72.5 hour week should be greater than 72");
        assertEquals(200, r6.getCost(), "200 cost expected");
    }

}
