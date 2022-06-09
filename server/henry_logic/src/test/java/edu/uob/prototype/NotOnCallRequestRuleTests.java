package edu.uob.prototype;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

final class NotOnCallRequestRuleTests {

    @Test
    void testBasicShiftsAndRules() {
        Shift s1 = new Shift(ShiftTypes.NotWorking);
        Shift s2 = new Shift(ShiftTypes.NightOnCall);
        Shift s3 = new Shift(ShiftTypes.DayOnCall);
        Shift s4 = new Shift(ShiftTypes.NormalShift);
        // Not working day
        Rule r1 = new NotOnCallRequestRule(s1);
        assertFalse(r1.isBroken(), "Not working should not break request for leave rule");
        assertEquals(0, r1.getCost(), "0 cost expected");
        // Night on call
        Rule r2 = new NotOnCallRequestRule(s2);
        assertTrue(r2.isBroken(), "On call night shift should break request for leave rule");
        assertTrue(r2.getCost()>0, ">0 cost expected");
        // Night on call
        Rule r3 = new NotOnCallRequestRule(s3);
        assertTrue(r3.isBroken(), "On call day shift should break request for leave rule");
        assertTrue(r3.getCost()>0, ">0 cost expected");
        // Night on call
        Rule r4 = new NotOnCallRequestRule(s4);
        assertTrue(r4.isBroken(), "Normal shift should break request for leave rule");
        assertTrue(r4.getCost()>0, ">0 cost expected");
    }
}
