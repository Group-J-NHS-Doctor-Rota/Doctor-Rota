package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnualLeaveRuleTests {

    @Test
    void testBasicShiftsAndRules() {
        Shift s1 = new Shift(ShiftTypes.NotWorking);
        Shift s2 = new Shift(ShiftTypes.NightOnCall);
        Shift s3 = new Shift(ShiftTypes.DayOnCall);
        Shift s4 = new Shift(ShiftTypes.NormalShift);
        // Not working day
        Rule r1 = new AnnualLeaveRule(s1);
        assertFalse(r1.isBroken(), "Not working should not break annual leave rule");
        assertEquals(0, r1.getCost(), "0 cost expected");
        // Night on call
        Rule r2 = new AnnualLeaveRule(s2);
        assertTrue(r2.isBroken(), "On call night shift should break annual leave rule");
        assertTrue(r2.getCost()>0, ">0 cost expected");
        // Night on call
        Rule r3 = new AnnualLeaveRule(s3);
        assertTrue(r3.isBroken(), "On call day shift should break annual leave rule");
        assertTrue(r3.getCost()>0, ">0 cost expected");
        // Night on call
        Rule r4 = new AnnualLeaveRule(s4);
        assertTrue(r4.isBroken(), "Normal shift should break annual leave rule");
        assertTrue(r4.getCost()>0, ">0 cost expected");
    }
}
