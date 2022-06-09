package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Max7ShiftsRuleTest {

    @Test
    void testBasicShiftsAndRules() {
        Shift s1 = new Shift(ShiftTypes.NotWorking);
        Shift s2 = new Shift(ShiftTypes.NormalShift);
        Shift s3 = new Shift(ShiftTypes.NightOnCall);
        Shift s4 = new Shift(ShiftTypes.DayOnCall);
        // Non-working week, rule should be met
        Rule r1 = new Max7ShiftsRule(s1, s1, s1, s1, s1, s1, s1, s1, s1, s1);
        assertFalse(r1.isBroken(), "No working, rule should be met");
        assertEquals(0, r1.getCost(), "0 cost expected");
        // 7 days of work then not working, rule should be met
        Rule r2 = new Max7ShiftsRule(s2, s2, s2, s2, s2, s2, s2, s1, s1, s1);
        assertFalse(r2.isBroken(), "0 hour week should meet rule");
        assertEquals(0, r2.getCost(), "0 cost expected");
        // 8 days of work, rule should not be met
        Rule r3 = new Max7ShiftsRule(s3, s3, s3, s3, s3, s3, s3, s3, s1, s1);
        assertTrue(r3.isBroken(), "8 nights shifts should break rule");
        assertTrue(r3.getCost()>0, ">0 cost expected");
        // 7 days of work with one not working day in middle, rule should be met
        Rule r4 = new Max7ShiftsRule(s4, s4, s4, s4, s4, s1, s4, s4, s1, s1);
        assertFalse(r4.isBroken(), "Rule should be met");
        assertEquals(0, r4.getCost(), "0 cost expected");
    }

}
